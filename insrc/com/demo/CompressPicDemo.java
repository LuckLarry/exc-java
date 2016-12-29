/**
 *  缩略图实现，将图片(jpg、bmp、png、gif等等)真实的变成想要的大小
 */
package com.demo;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class CompressPicDemo {
	/**
	 * 压缩图片[所有参数]
	 * 
	 * @param inputDir
	 * @param outputDir
	 * @param inputFileName
	 * @param outputFileName
	 * @param outputWidth
	 * @param outputHeight
	 * @param proportion
	 * @return
	 */
	public String doPicture(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int outputWidth,
			int outputHeight, boolean proportion) {
		try {
			// 如果file不存在
			File file = new File(inputDir + inputFileName);
			if (!file.exists()) {
				return null;
			}
			// 读取图片文件
			Image img = ImageIO.read(file);
			// 判断图片格式是否正确
			if (img.getWidth(null) == -1) {
				return "";
			} else {
				int newWidth;
				int newHeight;
				// 判断是否是等比缩放
				if (proportion == true) {
					// 为等比缩放计算输出的图片宽度及高度
					double rate1 = ((double) img.getWidth(null))
							/ (double) outputWidth + 0.1;
					double rate2 = ((double) img.getHeight(null))
							/ (double) outputHeight + 0.1;
					// 根据缩放比率大的进行缩放控制
					double rate = rate1 > rate2 ? rate1 : rate2;
					newWidth = (int) (((double) img.getWidth(null)) / rate);
					newHeight = (int) (((double) img.getHeight(null)) / rate);
				} else {
					// 输出的图片宽度
					newWidth = outputWidth;
					// 输出的图片高度
					newHeight = outputHeight;
				}
				BufferedImage tag = new BufferedImage((int) newWidth,
						(int) newHeight, BufferedImage.TYPE_INT_RGB);

				/*
				 * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的 优先级比速度高 生成的图片质量比较好 但速度慢
				 */
				tag.getGraphics().drawImage(
						img.getScaledInstance(newWidth, newHeight,
								Image.SCALE_SMOOTH), 0, 0, null);
				FileOutputStream out = new FileOutputStream(outputDir
						+ outputFileName);
				// JPEGImageEncoder可适用于其他图片类型的转换
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
				ImageIO.write(tag, "jpeg", out);
//				encoder.encode(tag);
				out.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return outputDir + outputFileName;
	}

	/**
	 * 压缩图片[传参：输入图路径,输入图文件名,输出图路径,输出图文件名,           宽,高]
	 * 
	 * @param inputDir
	 * @param inputFileName
	 * @param outputDir
	 * @param outputFileName
	 * @return
	 */
	public String doPicture(String inputDir, String inputFileName,
			String outputDir, String outputFileName,int outputWidth,
			int outputHeight) {
		return doPicture(inputDir, outputDir, inputFileName, outputFileName,
				outputWidth, outputHeight, false);
	}
	
	/**
	 * 压缩图片[传参：输入图路径,输入图文件名,输出图路径,输出图文件名]
	 * 
	 * @param inputDir
	 * @param inputFileName
	 * @param outputDir
	 * @param outputFileName
	 * @return
	 */
	public String doPicture(String inputDir, String inputFileName,
			String outputDir, String outputFileName) {
		return doPicture(inputDir, outputDir, inputFileName, outputFileName,
				100, 100);
	}
	/**
	 * 
	 * 压缩图片[传参：输入图路径,输入图文件名]
	 * 
	 * @param inputDir
	 * @param inputFileName
	 * @return
	 */
	public String doPicture(String inputDir, String inputFileName) {
		String outputFileName = inputFileName.replace(".", "_s.");
		return doPicture(inputDir, inputFileName, inputDir, outputFileName);
	}

	/**
	 * 压缩图片[传参：输入图路径,输入图文件名,          宽,高]
	 * 
	 * @param inputDir
	 * @param inputFileName
	 * @return
	 */
	public String doPicture(String inputDir, String inputFileName, int outputWidth,int outputHeight) {
		String outputFileName = inputFileName.replace(".", "_s.");
		return doPicture(inputDir, inputFileName, inputDir, outputFileName, outputWidth, outputHeight);
	}

	/**
	 * into 压缩图片[只传图片路径  固定图片比例为100*100]
	 * 
	 * @param inputDir
	 * @return
	 */
	public String doPicture(String inputDir) {
		String inputFileName = inputDir.substring(
				inputDir.lastIndexOf("/") + 1, inputDir.length());
		inputDir = inputDir.substring(0, inputDir.lastIndexOf("/") + 1);
		return doPicture(inputDir, inputFileName);
	}
	

	/**
	 * into 压缩图片[传图片路径,         宽,高]
	 * 
	 * @param inputDir
	 * @return
	 */
	public String doPicture(String inputDir,int outputWidth,int outputHeight) {
		String inputFileName = inputDir.substring(
				inputDir.lastIndexOf("/") + 1, inputDir.length());
		inputDir = inputDir.substring(0, inputDir.lastIndexOf("/") + 1);
		return doPicture(inputDir, inputFileName,outputWidth,outputHeight);
	}
}