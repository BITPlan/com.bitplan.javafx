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
package com.bitplan.javafx;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import com.bitplan.error.ErrorHandler;
import com.bitplan.gui.Display;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.stage.Screen;
import javafx.stage.Stage;
import sun.awt.image.IntegerComponentRaster;

/**
 * Waitable Application that does not need launch
 * 
 * @author wf
 *
 */
@SuppressWarnings("restriction")
public abstract class WaitableApp extends Application implements Display {
	protected static Logger LOGGER = Logger.getLogger("com.bitplan.javafx");
	public static boolean debug = false;
	public static boolean testMode = false;
	protected Stage stage;
	static boolean toolkitStarted;
	File screenShot = null;

	public static double getScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}

	public static double getScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}

	/**
	 * allow startup without launch
	 */
	public static void toolkitInit() {
		if (!toolkitStarted) {
			toolkitStarted = true;
			// do not exit on close of last window
			// https://stackoverflow.com/a/10217157/1497139
			Platform.setImplicitExit(false);
			/// https://stackoverflow.com/a/38883432/1497139
			// http://www.programcreek.com/java-api-examples/index.php?api=com.sun.javafx.application.PlatformImpl
			com.sun.javafx.application.PlatformImpl.startup(() -> {
			});
		}
	}

	/**
	 * get SceneBounds
	 * 
	 * @param screenPercent
	 * @return the scene bounds
	 */
	public Rectangle2D getSceneBounds(int screenPercent, int xDiv, int yDiv) {
		double sceneWidth = getScreenWidth() * screenPercent / 100.0;
		double sceneHeight = getScreenHeight() * screenPercent / 100.0;
		double x = (getScreenWidth() - sceneWidth) / xDiv;
		double y = (getScreenHeight() - sceneHeight) / yDiv;
		Rectangle2D sceneBounds = new Rectangle2D(x, y, sceneWidth, sceneHeight);
		return sceneBounds;
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void browse(String url) {
		this.getHostServices().showDocument(url);
	}

	public static int waitTimeOutSecs = 120;

	public void abortWait() {
		LOGGER.log(Level.WARNING, "wait aborted after " + waitTimeOutSecs + " secs");
		if (!testMode)
			System.exit(1);
	}

	/**
	 * wait for close
	 * 
	 * @throws InterruptedException
	 */
	public void waitStatus(boolean open) {
		int sleep = 1000 / 50; // human eye reaction time
		int timeout = 0;
		try {
			if (open)
				while ((stage == null) || (!stage.isShowing())) {
					Thread.sleep(sleep);
					timeout += sleep;
					if (timeout >= waitTimeOutSecs * 1000) {
						abortWait();
						break;
					}
				}
			else
				while (stage != null && stage.isShowing()) {
					Thread.sleep(sleep);
					timeout += sleep;
				}
		} catch (InterruptedException e) {
			ErrorHandler.handle(e);
		}
	}

	public void waitOpen() {
		waitStatus(true);
	}

	public void waitClose() {
		waitStatus(false);
	}

	/**
	 * show me
	 */
	public void show() {
		// ignore multiple calls
		if (stage != null) {
			LOGGER.log(Level.WARNING, "show called with active stage " + stage);
			return;
		}
		Platform.runLater(() -> {
			try {
				Stage lStage = new Stage();
				this.start(lStage);
			} catch (Exception e) {
				ErrorHandler.handle(e);
			}
		});
	}

	/**
	 * limit the show Time
	 * 
	 * @param showTimeSecs
	 */
	public void limitShowTime(int showTimeSecs) {
		new Timer().schedule(new TimerTask() {

			@Override
			public void run() {
				close();
			}
		}, showTimeSecs * 1000);
	}

	/**
	 * close this display
	 */
	public void close() {
		Platform.runLater(() -> {
			if (stage != null) {
				stage.close();
			}
		});
		this.waitClose();
		// allow reopening
		stage = null;
	}

	/**
	 * save me as a Png File
	 * 
	 * @param stage
	 *            - the stage to take a screen shot from
	 * @param file
	 *            - the file to save the image to
	 */
	public static synchronized void saveAsPng(Stage stage, File file) {
		saveAsPng(stage.getScene().getRoot(), file);
	}

	/**
	 * save a snapshot of the given node as a PNG file
	 * @param node - the node to get a snapshot from
	 * @param file - the file to save to
	 */
	public static synchronized void saveAsPng(Node node, File file) {
		Bounds bounds = node.getBoundsInParent();
		WritableImage writableImage = new WritableImage((int) bounds.getWidth(), (int) bounds.getHeight());
		SnapshotParameters params = new SnapshotParameters();
		node.snapshot(params, writableImage);
		try {
			ImageIO.write(fromFXImage(writableImage, null), "png", file);
		} catch (Throwable th) {
			ErrorHandler.handle(th);
		}
	}

	/**
	 * Snapshots the specified JavaFX {@link Image} object and stores a copy of its
	 * pixels into a {@link BufferedImage} object, creating a new object if needed.
	 * The method will only convert a JavaFX {@code Image} that is readable as per
	 * the conditions on the {@link Image#getPixelReader() Image.getPixelReader()}
	 * method. If the {@code Image} is not readable, as determined by its
	 * {@code getPixelReader()} method, then this method will return null. If the
	 * {@code Image} is a writable, or other dynamic image, then the
	 * {@code BufferedImage} will only be set to the current state of the pixels in
	 * the image as determined by its {@link PixelReader}. Further changes to the
	 * pixels of the {@code Image} will not be reflected in the returned
	 * {@code BufferedImage}.
	 * <p>
	 * The optional {@code BufferedImage} parameter may be reused to store the copy
	 * of the pixels. A new {@code BufferedImage} will be created if the supplied
	 * object is null, is too small or of a type which the image pixels cannot be
	 * easily converted into.
	 * 
	 * @param img
	 *            the JavaFX {@code Image} to be converted
	 * @param bimg
	 *            an optional {@code BufferedImage} object that may be used to store
	 *            the returned pixel data
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
		if (bimg != null) {
			int type = bimg.getType();
			int bw = bimg.getWidth();
			int bh = bimg.getHeight();
			if (bw < iw || bh < ih
					|| (type != BufferedImage.TYPE_INT_ARGB && type != BufferedImage.TYPE_INT_ARGB_PRE)) {
				bimg = null;
			} else if (iw < bw || ih < bh) {
				Graphics2D g2d = bimg.createGraphics();
				g2d.setComposite(AlphaComposite.Clear);
				g2d.fillRect(0, 0, bw, bh);
				g2d.dispose();
			}
		}
		if (bimg == null) {
			bimg = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB_PRE);
		}
		IntegerComponentRaster icr = (IntegerComponentRaster) bimg.getRaster();
		int offset = icr.getDataOffset(0);
		int scan = icr.getScanlineStride();
		int data[] = icr.getDataStorage();
		WritablePixelFormat<IntBuffer> pf = (bimg.isAlphaPremultiplied() ? PixelFormat.getIntArgbPreInstance()
				: PixelFormat.getIntArgbInstance());
		pr.getPixels(0, 0, iw, ih, pf, data, offset, scan);
		return bimg;
	}

}
