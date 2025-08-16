package com.giun.ecs.service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.giun.ecs.dto.request.ProductUploadReq;
import com.giun.ecs.dto.response.Outbound;
import com.giun.ecs.dto.response.ProductResp;
import com.giun.ecs.dto.response.ProductShow;
import com.giun.ecs.entity.Product;
import com.giun.ecs.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public Outbound saveProduct(ProductUploadReq req) {
		ImageInfo imageInfo = processBase64Image(req.getImageBase64(), req.getImageType());

		Product product = Product.builder()
				.name(req.getName())
				.category(req.getCategory())
				.price(req.getPrice())
				.stock(req.getStock())
				.description(req.getDescription())
				.imageData(imageInfo.imageData)
				.imageType(imageInfo.imageType)
				.build();

		Product newProduct = productRepository.save(product);

		return Outbound.ok(newProduct);
	}

	public Outbound getProductById(Integer id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

		ProductResp response = ProductResp.builder()
				.id(product.getId())
				.name(product.getName())
				.price(product.getPrice())
				.stock(product.getStock())
				.description(product.getDescription())
				.category(product.getCategory())
				.imageBase64(generateImageBase64(product.getImageData(), product.getImageType()))
				.build();

		return Outbound.ok(response);
	}

	public Outbound getAllProducts() {
		List<ProductShow> result = productRepository.findAll().stream().map(product -> {

			return ProductShow.builder()
					.id(product.getId())
					.name(product.getName())
					.price(product.getPrice())
					.description(product.getDescription())
					.category(product.getCategory())
					.rating(null) // TODO: 根據實際資料庫欄位填入 product.getRating()
					.imageBase64(generateImageBase64(product.getImageData(), product.getImageType()))
					.build();
		}).collect(Collectors.toList());

		return Outbound.ok(result);
	}

	public Outbound updateProduct(Integer id, ProductUploadReq req) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

		ImageInfo imageInfo = processBase64Image(req.getImageBase64(), req.getImageType());

		Product updateProduct = Product.builder()
				.id(product.getId())
				.name(req.getName())
				.category(req.getCategory())
				.stock(req.getStock())
				.price(req.getPrice())
				.description(req.getDescription())
				.imageData(imageInfo.imageData)
				.imageType(imageInfo.imageType)
				.build();

		productRepository.save(updateProduct);

		return Outbound.ok("Product updated successfully");
	}

	public Outbound productList() {

		List<ProductResp> result = productRepository.findAll().stream().map(product -> {

			return ProductResp.builder()
					.id(product.getId())
					.name(product.getName())
					.price(product.getPrice())
					.stock(product.getStock())
					.description(product.getDescription())
					.category(product.getCategory())
					.imageBase64(generateImageBase64(product.getImageData(), product.getImageType()))
					.build();
		}).collect(Collectors.toList());

		return Outbound.ok(result);
	}

	public Outbound deleteProduct(Integer id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));

		if (product != null) {
			productRepository.updateProductStates(id, "0");
		}

		return Outbound.ok("Product u successfully");
	}

	/**
	 * 用來傳遞圖片處理結果的 record。 Record 是 Java 14+ 的特性，適合用來傳遞不可變的資料物件。
	 */
	private record ImageInfo(byte[] imageData, String imageType) {
	}

	/**
	 * 處理 Base64 圖片字串，解析出圖片二進制資料和類型。
	 * 
	 * @param base64String      Base64 編碼的圖片字串，可包含 Data URI 前綴。
	 * @param existingImageType 已知或預設的圖片類型。
	 * @return 包含圖片資料和類型的 ImageInfo 物件。
	 */
	private ImageInfo processBase64Image(String base64String, String existingImageType) {
		if (base64String == null || base64String.isBlank()) {
			return new ImageInfo(null, null); // 沒有圖片，返回空值
		}

		String imageType = existingImageType;
		String base64Content = base64String;

		// 移除 Data URI scheme 前綴並嘗試解析圖片類型
		if (base64String.startsWith("data:")) {
			int commaIndex = base64String.indexOf(',');
			if (commaIndex != -1) {
				String dataUri = base64String.substring(0, commaIndex);
				if (dataUri.contains(";base64")) {
					imageType = dataUri.substring(dataUri.indexOf(':') + 1, dataUri.indexOf(';'));
				}
				base64Content = base64String.substring(commaIndex + 1);
			}
		}

		try {
			byte[] imageBytes = Base64.getDecoder().decode(base64Content);
			return new ImageInfo(imageBytes, imageType);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("無效的 Base64 圖片格式", e);
		}
	}

	/**
	 * 產生圖片 Base64 字串
	 * 
	 * @param imageData 圖片資料
	 * @param imageType 圖片類型
	 * @return
	 */
	private String generateImageBase64(byte[] imageData, String imageType) {
		return imageData != null && imageType != null
				? "data:" + imageType + ";base64,"
						+ Base64.getEncoder().encodeToString(imageData)
				: null;
	}
}
