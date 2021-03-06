/**
 *
 * This file is part of the https://github.com/BITPlan/com.bitplan.javafx open source project
 *
 * The copyright and license below holds true
 * for all files except
 *    - the ones in the stackoverflow package
 *    - SwingFXUtils.java from Oracle which is provided e.g. for the raspberry env
 *
 * Copyright 2017-2018 BITPlan GmbH https://github.com/BITPlan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 *  You may obtain a copy of the License at
 *
 *  http:www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// see http://cr.openjdk.java.net/~ant/RT-37740/webrev.0/modules/swing/src/main/java/javafx/embed/swing/SwingFXUtils.java.html
//    1 /*
//   2  * Copyright (c) 2012, 2014, Oracle and/or its affiliates. All rights reserved.
//   3  * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//   4  *
//   5  * This code is free software; you can redistribute it and/or modify it
//   6  * under the terms of the GNU General Public License version 2 only, as
//   7  * published by the Free Software Foundation.  Oracle designates this
//   8  * particular file as subject to the "Classpath" exception as provided
//   9  * by Oracle in the LICENSE file that accompanied this code.
//  10  *
//  11  * This code is distributed in the hope that it will be useful, but WITHOUT
//  12  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  13  * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
//  14  * version 2 for more details (a copy is included in the LICENSE file that
//  15  * accompanied this code).
//  16  *
//  17  * You should have received a copy of the GNU General Public License version
//  18  * 2 along with this work; if not, write to the Free Software Foundation,
//  19  * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//  20  *
//  21  * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
//  22  * or visit www.oracle.com if you need additional information or have any
//  23  * questions.
//  24  */
package com.bitplan.javafx;

import java.awt.AlphaComposite;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.SecondaryLoop;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit;
import com.sun.glass.ui.Pixels;
import sun.awt.AWTAccessor;
import sun.awt.FwDispatcher;
import sun.awt.image.IntegerComponentRaster;

import javax.swing.*;

/**
 * This class provides utility methods for converting data types between
 * Swing/AWT and JavaFX formats.
 * 
 * @since JavaFX 2.2
 */
public class SwingFXUtils {
  private SwingFXUtils() {
  } // no instances

  private static volatile int fxPixelsNativeFormat = -1; // initially undefined

  /**
   * Must be called on the FX event thread.
   */
  static void notifyFXInitialized() {
    if (!PlatformImpl.isFxApplicationThread()) {
      return;
    }
    fxPixelsNativeFormat = Pixels.getNativeFormat();
  }

  /**
   * Snapshots the specified {@link BufferedImage} and stores a copy of its
   * pixels into a JavaFX {@link Image} object, creating a new object if needed.
   * The returned {@code Image} will be a static snapshot of the state of the
   * pixels in the {@code BufferedImage} at the time the method completes.
   * Further changes to the {@code BufferedImage} will not be reflected in the
   * {@code Image}.
   * <p>
   * The optional JavaFX {@link WritableImage} parameter may be reused to store
   * the copy of the pixels. A new {@code Image} will be created if the supplied
   * object is null, is too small or of a type which the image pixels cannot be
   * easily converted into.
   * 
   * @param bimg
   *          the {@code BufferedImage} object to be converted
   * @param wimg
   *          an optional {@code WritableImage} object that can be used to store
   *          the returned pixel data
   * @return an {@code Image} object representing a snapshot of the current
   *         pixels in the {@code BufferedImage}.
   * @since JavaFX 2.2
   */
  public static WritableImage toFXImage(BufferedImage bimg,
      WritableImage wimg) {
    int bw = bimg.getWidth();
    int bh = bimg.getHeight();
    switch (bimg.getType()) {
    case BufferedImage.TYPE_INT_ARGB:
    case BufferedImage.TYPE_INT_ARGB_PRE:
      break;
    default:
      BufferedImage converted = new BufferedImage(bw, bh,
          BufferedImage.TYPE_INT_ARGB_PRE);
      Graphics2D g2d = converted.createGraphics();
      g2d.drawImage(bimg, 0, 0, null);
      g2d.dispose();
      bimg = converted;
      break;
    }
    // assert(bimg.getType == TYPE_INT_ARGB[_PRE]);
    if (wimg != null) {
      int iw = (int) wimg.getWidth();
      int ih = (int) wimg.getHeight();
      if (iw < bw || ih < bh) {
        wimg = null;
      } else if (bw < iw || bh < ih) {
        int empty[] = new int[iw];
        PixelWriter pw = wimg.getPixelWriter();
        PixelFormat<IntBuffer> pf = PixelFormat.getIntArgbPreInstance();
        if (bw < iw) {
          pw.setPixels(bw, 0, iw - bw, bh, pf, empty, 0, 0);
        }
        if (bh < ih) {
          pw.setPixels(0, bh, iw, ih - bh, pf, empty, 0, 0);
        }
      }
    }
    if (wimg == null) {
      wimg = new WritableImage(bw, bh);
    }
    PixelWriter pw = wimg.getPixelWriter();
    IntegerComponentRaster icr = (IntegerComponentRaster) bimg.getRaster();
    int data[] = icr.getDataStorage();
    int offset = icr.getDataOffset(0);
    int scan = icr.getScanlineStride();
    PixelFormat<IntBuffer> pf = (bimg.isAlphaPremultiplied()
        ? PixelFormat.getIntArgbPreInstance()
        : PixelFormat.getIntArgbInstance());
    pw.setPixels(0, 0, bw, bh, pf, data, offset, scan);
    return wimg;
  }

  /**
   * Creates a BufferedImage object compatible with the FX pixels format.
   * 
   * @param w
   *          the width of the buffer
   * @param h
   *          the height of the buffer
   * @return the new buffer created or null until FX platform is initialized
   */
  static BufferedImage createCompatibleBufferedImage(int w, int h) {
    if (fxPixelsNativeFormat == -1) {
      return null;
    }
    ByteOrder byteorder = ByteOrder.nativeOrder();
    switch (fxPixelsNativeFormat) {
    case Pixels.Format.BYTE_BGRA_PRE:
      if (byteorder == ByteOrder.LITTLE_ENDIAN) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB_PRE);
      } else {
        throw new UnsupportedOperationException(
            "BYTE_BGRA_PRE pixel format on BIG_ENDIAN");
      }
    case Pixels.Format.BYTE_ARGB:
      if (byteorder == ByteOrder.BIG_ENDIAN) {
        return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
      } else {
        throw new UnsupportedOperationException(
            "BYTE_ARGB pixel format on LITTLE_ENDIAN");
      }
    default:
      throw new UnsupportedOperationException(
          "unrecognized pixel format: " + fxPixelsNativeFormat);
    }
  }

  /**
   * Determine the optimal BufferedImage type to use for the specified
   * {@code fxFormat} allowing for the specified {@code bimg} to be used as a
   * potential default storage space if it is not null and is compatible.
   * 
   * @param fxFormat
   *          the PixelFormat of the source FX Image
   * @param bimg
   *          an optional existing {@code BufferedImage} to be used for storage
   *          if it is compatible, or null
   * @return
   */
  private static int getBestBufferedImageType(PixelFormat<?> fxFormat,
      BufferedImage bimg) {
    if (bimg != null) {
      int bimgType = bimg.getType();
      if (bimgType == BufferedImage.TYPE_INT_ARGB
          || bimgType == BufferedImage.TYPE_INT_ARGB_PRE) {
        // We will allow the caller to give us a BufferedImage
        // that has an alpha channel, but we might not otherwise
        // construct one ourselves.
        // We will also allow them to choose their own premultiply
        // type which may not match the image.
        // If left to our own devices we might choose a more specific
        // format as indicated by the choices below.
        return bimgType;
      }
    }
    switch (fxFormat.getType()) {
    default:
    case BYTE_BGRA_PRE:
    case INT_ARGB_PRE:
      return BufferedImage.TYPE_INT_ARGB_PRE;
    case BYTE_BGRA:
    case INT_ARGB:
      return BufferedImage.TYPE_INT_ARGB;
    case BYTE_RGB:
      return BufferedImage.TYPE_INT_RGB;
    case BYTE_INDEXED:
      return (fxFormat.isPremultiplied() ? BufferedImage.TYPE_INT_ARGB_PRE
          : BufferedImage.TYPE_INT_ARGB);
    }
  }

  /**
   * Determine the appropriate {@link WritablePixelFormat} type that can be used
   * to transfer data into the indicated BufferedImage.
   * 
   * @param bimg
   *          the BufferedImage that will be used as a destination for a
   *          {@code PixelReader<IntBuffer>#getPixels()} operation.
   * @return
   */
  private static WritablePixelFormat<IntBuffer> getAssociatedPixelFormat(
      BufferedImage bimg) {
    switch (bimg.getType()) {
    // We lie here for xRGB, but we vetted that the src data was opaque
    // so we can ignore the alpha. We use ArgbPre instead of Argb
    // just to get a loop that does not have divides in it if the
    // PixelReader happens to not know the data is opaque.
    case BufferedImage.TYPE_INT_RGB:
    case BufferedImage.TYPE_INT_ARGB_PRE:
      return PixelFormat.getIntArgbPreInstance();
    case BufferedImage.TYPE_INT_ARGB:
      return PixelFormat.getIntArgbInstance();
    default:
      // Should not happen...
      throw new InternalError("Failed to validate BufferedImage type");
    }
  }

  /**
   * Snapshots the specified JavaFX {@link Image} object and stores a copy of
   * its pixels into a {@link BufferedImage} object, creating a new object if
   * needed. The method will only convert a JavaFX {@code Image} that is
   * readable as per the conditions on the {@link Image#getPixelReader()
   * Image.getPixelReader()} method. If the {@code Image} is not readable, as
   * determined by its {@code getPixelReader()} method, then this method will
   * return null. If the {@code Image} is a writable, or other dynamic image,
   * then the {@code BufferedImage} will only be set to the current state of the
   * pixels in the image as determined by its {@link PixelReader}. Further
   * changes to the pixels of the {@code Image} will not be reflected in the
   * returned {@code BufferedImage}.
   * <p>
   * The optional {@code BufferedImage} parameter may be reused to store the
   * copy of the pixels. A new {@code BufferedImage} will be created if the
   * supplied object is null, is too small or of a type which the image pixels
   * cannot be easily converted into.
   * 
   * @param img
   *          the JavaFX {@code Image} to be converted
   * @param bimg
   *          an optional {@code BufferedImage} object that may be used to store
   *          the returned pixel data
   * @return a {@code BufferedImage} containing a snapshot of the JavaFX
   *         {@code Image}, or null if the {@code Image} is not readable.
   * @since JavaFX 2.2
   */
  public static BufferedImage fromFXImage(Image img, BufferedImage bimg) {
    PixelReader pr = img.getPixelReader();
    if (pr == null) {
      return null;
    }
    int iw = (int) img.getWidth();
    int ih = (int) img.getHeight();
    int prefBimgType = getBestBufferedImageType(pr.getPixelFormat(), bimg);
    if (bimg != null) {
      int bw = bimg.getWidth();
      int bh = bimg.getHeight();
      if (bw < iw || bh < ih || bimg.getType() != prefBimgType) {
        bimg = null;
      } else if (iw < bw || ih < bh) {
        Graphics2D g2d = bimg.createGraphics();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.fillRect(0, 0, bw, bh);
        g2d.dispose();
      }
    }
    if (bimg == null) {
      bimg = new BufferedImage(iw, ih, prefBimgType);
    }
    IntegerComponentRaster icr = (IntegerComponentRaster) bimg.getRaster();
    int offset = icr.getDataOffset(0);
    int scan = icr.getScanlineStride();
    int data[] = icr.getDataStorage();
    WritablePixelFormat<IntBuffer> pf = getAssociatedPixelFormat(bimg);
    pr.getPixels(0, 0, iw, ih, pf, data, offset, scan);
    return bimg;
  }

  /**
   * If called from the FX Application Thread invokes a runnable directly
   * blocking the calling code Otherwise uses Platform.runLater without blocking
   */
  static void runOnFxThread(Runnable runnable) {
    if (Platform.isFxApplicationThread()) {
      runnable.run();
    } else {
      Platform.runLater(runnable);
    }
  }

  /**
   * If called from the event dispatch thread invokes a runnable directly
   * blocking the calling code Otherwise uses SwingUtilities.invokeLater without
   * blocking
   */
  static void runOnEDT(final Runnable r) {
    if (SwingUtilities.isEventDispatchThread()) {
      r.run();
    } else {
      SwingUtilities.invokeLater(r);
    }
  }

  private static final Set<Object> eventLoopKeys = new HashSet<>();

  /**
   * The runnable is responsible for leaving the nested event loop.
   */
  static void runOnEDTAndWait(Object nestedLoopKey, Runnable r) {
    Toolkit.getToolkit().checkFxUserThread();

    if (SwingUtilities.isEventDispatchThread()) {
      r.run();
    } else {
      eventLoopKeys.add(nestedLoopKey);
      SwingUtilities.invokeLater(r);
      Toolkit.getToolkit().enterNestedEventLoop(nestedLoopKey);
    }
  }

  static void leaveFXNestedLoop(Object nestedLoopKey) {
    if (!eventLoopKeys.contains(nestedLoopKey))
      return;

    if (Platform.isFxApplicationThread()) {
      Toolkit.getToolkit().exitNestedEventLoop(nestedLoopKey, null);
    } else {
      Platform.runLater(() -> {
        Toolkit.getToolkit().exitNestedEventLoop(nestedLoopKey, null);
      });
    }

    eventLoopKeys.remove(nestedLoopKey);
  }

  private static class FwSecondaryLoop implements SecondaryLoop {

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    @Override
    public boolean enter() {
      if (isRunning.compareAndSet(false, true)) {
        PlatformImpl.runAndWait(() -> {
          Toolkit.getToolkit().enterNestedEventLoop(FwSecondaryLoop.this);
        });
        return true;
      }
      return false;
    }

    @Override
    public boolean exit() {
      if (isRunning.compareAndSet(true, false)) {
        PlatformImpl.runAndWait(() -> {
          Toolkit.getToolkit().exitNestedEventLoop(FwSecondaryLoop.this, null);
        });
        return true;
      }
      return false;
    }
  }

  private static class FXDispatcher implements FwDispatcher {
    @Override
    public boolean isDispatchThread() {
      return Platform.isFxApplicationThread();
    }

    @Override
    public void scheduleDispatch(Runnable runnable) {
      Platform.runLater(runnable);
    }

    @Override
    public SecondaryLoop createSecondaryLoop() {
      return new FwSecondaryLoop();
    }
  }

  private static EventQueue getEventQueue() {
    return AccessController
        .doPrivileged((PrivilegedAction<EventQueue>) () -> java.awt.Toolkit
            .getDefaultToolkit().getSystemEventQueue());
  }

  // Called with reflection from PlatformImpl to avoid dependency
  private static void installFwEventQueue() {
    AWTAccessor.getEventQueueAccessor().setFwDispatcher(getEventQueue(),
        new FXDispatcher());
  }

  // Called with reflection from PlatformImpl to avoid dependency
  private static void removeFwEventQueue() {
    AWTAccessor.getEventQueueAccessor().setFwDispatcher(getEventQueue(), null);
  }
}
