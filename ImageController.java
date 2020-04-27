package com.baris.shopping.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.baris.shopping.helper.ImageCropper;
import com.baris.user.service.ResourceNotFoundException;

@RestController
public class ImageController {

	@Value("${storage.staticPath}")
	public String staticPath;

	@GetMapping(value = "/image2/{imageWidth}x{imageHeight}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<InputStreamResource> getImage(@PathVariable("imageWidth") int imageWidth,
			@PathVariable("imageHeight") int imageHeight, @PathVariable("fileName") String fileName) {

		String inputImagePath = this.staticPath + "productimages/";
		String outputImagePath = this.staticPath + "image/";

		String inputImage 		= inputImagePath + fileName;
		String outputImage 		= outputImagePath + imageWidth + "x" + imageHeight + "/" + fileName;

		File fileInput = new File(inputImage);
		File fileOutput = new File(outputImage);

		if (!fileInput.exists()) {
			throw new ResourceNotFoundException("the file doesn't exists");
		} else if (!fileOutput.exists()) {
			try {
				ImageCropper iCropper = new ImageCropper();
				iCropper.cropImageToGivenSize(inputImage, outputImage, imageWidth, imageHeight);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
					.body(new InputStreamResource(new FileInputStream(fileOutput)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@GetMapping(value = "/image/{imageWidth}x{imageHeight}/{fileName}", produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<InputStreamResource> getImageWithWhite(@PathVariable("imageWidth") int imageWidth,
			@PathVariable("imageHeight") int imageHeight, @PathVariable("fileName") String fileName) {

		String inputImagePath = this.staticPath + "productimages/";
		String outputImagePath = this.staticPath + "image/";

		String inputImage 		= inputImagePath + fileName;
		String outputImage 		= outputImagePath + imageWidth + "x" + imageHeight + "/" + fileName;

		File fileInput = new File(inputImage);
		File fileOutput = new File(outputImage);

		if (!fileInput.exists()) {
			throw new ResourceNotFoundException("the file doesn't exists");
		} else if (!fileOutput.exists()) {
			ImageCropper iCropper = new ImageCropper();
			try {
				iCropper.resizeImageWithWhite(inputImage, outputImage, outputImagePath, imageWidth, imageHeight);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
					.body(new InputStreamResource(new FileInputStream(fileOutput)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
