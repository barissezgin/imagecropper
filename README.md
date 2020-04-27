# image cropper in java
this class contains two method.

## cropImageToGivenSize(String inputImagePath, String outputImagePath, int targetWidth, int targetHeight)
it can be used to resize an image by keeping its ratio.

## BufferedImage resizeImage(BufferedImage originalImage, int type, Integer img_width, Integer img_height)
it can be used to resize an image by keeping its ratio. However, if you happen to give a different ratio, the uncovered space will be filled with a given color, which is white by default
