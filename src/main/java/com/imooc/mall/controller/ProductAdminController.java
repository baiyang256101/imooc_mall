package com.imooc.mall.controller;

import com.imooc.mall.common.ApiRestResponse;
import com.imooc.mall.common.Constant;
import com.imooc.mall.exception.ImoocMallException;
import com.imooc.mall.exception.ImoocMallExceptionEnum;
import com.imooc.mall.model.reuqest.AddProductReq;
import com.imooc.mall.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
public class ProductAdminController {

    @Resource
    ProductService productService;

    /**
     * 商品添加
     *
     * @param addProductReq
     * @return
     */
    @PostMapping("admin/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    /**
     * 图片上传
     *
     * @param httpServletRequest
     * @param file
     * @return
     */
    @PostMapping("admin/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws IOException {
        // 获取文件名
        String filename = file.getOriginalFilename();
        // 图片后缀名
        String suffixName = filename.substring(filename.lastIndexOf("."));
        // UUID
        UUID uuid = UUID.randomUUID();
        String newFileName = uuid.toString() + suffixName;

        // 创建文件夹
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);

        // 生成文件
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);

        if (!fileDirectory.exists()) {
            if (!fileDirectory.mkdir()) {
                throw new ImoocMallException(ImoocMallExceptionEnum.MKDIR_FAILED);
            }
        }
        // 传参进来的 fire 写到空文件中
        file.transferTo(destFile);

        // 返回文件地址
        try {
            // 路径：ip+端口号
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/" + newFileName);
        } catch (URISyntaxException e) {
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    /**
     * 裁剪 URL方法
     *
     * @param uri
     * @return
     */
    private URI getHost(URI uri) {
        URI effective;
        try {
            effective = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) { // 新建的过程失败了，设置为 null
            effective = null;
        }
        return effective;
    }

    @PostMapping("admin/test")
    public ApiRestResponse test() {
        return ApiRestResponse.success();
    }
}
