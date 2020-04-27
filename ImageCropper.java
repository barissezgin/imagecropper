package com.baris.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ImageCropper {

	public String cropImageToGivenSize(String inputImagePath, String outputImagePath, int targetWidth, int targetHeight)
			throws IOException {
		BufferedImage originalImage = ImageIO.read(new File(inputImagePath));

		// if width or height is equals 0, image is being resizing original ratio
		if (targetWidth==0) {
			targetWidth = originalImage.getWidth();
			targetHeight = originalImage.getHeight();
		} else if (targetHeight==0) {
    		targetHeight = targetWidth*originalImage.getHeight()/originalImage.getWidth();
		}
		// resizing is being end 
		
		float widthRatio = (float) originalImage.getWidth() / targetWidth;
		float heightRatio = (float) originalImage.getHeight() / targetHeight;

		BufferedImage resizedImage = originalImage;
		int resizedWidth = originalImage.getWidth();
		int resizedHeight = originalImage.getHeight();
		if (widthRatio > heightRatio) { // shrink to fixed height
			resizedWidth = Math.round(originalImage.getWidth() / heightRatio);
			resizedHeight = targetHeight;
		} else { // shrink to fixed width
			resizedWidth = targetWidth;
			resizedHeight = Math.round(originalImage.getHeight() / widthRatio);
		}
		resizedImage = resizeImage(originalImage, originalImage.getType(), resizedWidth, resizedHeight);

		int startX = resizedWidth / 2 - targetWidth / 2;
		int startY = resizedHeight / 2 - targetHeight / 2;
		BufferedImage SubImage = resizedImage.getSubimage(startX, startY, targetWidth, targetHeight);

		File outputfile = new File(outputImagePath);
		ImageIO.write(SubImage, "jpg", outputfile);

		return outputfile.getPath();
	}

	public BufferedImage resizeImage(BufferedImage originalImage, int type, Integer img_width,
			Integer img_height) {
		BufferedImage resizedImage = new BufferedImage(img_width, img_height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, img_width, img_height, null);
		g.dispose();

		return resizedImage;
	}

	public void resizeImageWithWhite(String inputImage, String outputImage,
									 String outputImagePath, int targetWidth, int targetHeight)
			throws IOException {

		// load source images
		BufferedImage image = ImageIO.read(new File(inputImage));

		// create the new image, canvas size is the max. of both image sizes
		BufferedImage combined = new BufferedImage(targetWidth, targetHeight, image.getType());

		// paint both images, preserving the alpha channels
		int[] ratios = aspectRatio(image.getWidth(), image.getHeight(), targetWidth, targetHeight);
		int lastWidth = ratios[0];
		int lastHeight = ratios[1];

		int newW = ((targetWidth-lastWidth) / 2);
		int newH = ((targetHeight-lastHeight) / 2);

		Graphics g = combined.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0,0,550,550);
		g.drawImage(image, newW, newH, lastWidth, lastHeight, null);
		g.dispose();

		// Save as new image
		ImageIO.write(combined, "jpg", new File(outputImage));
	}

	public int[] aspectRatio(int width, int height, int targetWidth, int targetHeight) {
		int lastWidth = width;
		int lastHeight = height;

		if (lastWidth > targetWidth) {
			lastHeight = targetWidth * height / width;
			lastWidth = targetWidth;
		}
		if (lastHeight > targetHeight) {
			lastWidth = width * targetHeight / height;
			lastHeight = targetHeight;
		}
		int[] ratios = {lastWidth, lastHeight};
		return ratios;
	}
}