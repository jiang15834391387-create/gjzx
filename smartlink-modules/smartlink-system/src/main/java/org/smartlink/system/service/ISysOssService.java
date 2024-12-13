package org.smartlink.system.service;

import jakarta.servlet.http.HttpServletResponse;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.system.domain.bo.SysOssBo;
import org.smartlink.system.domain.vo.SysOssVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * 文件上传 服务层
 *
 * @author Lion Li
 */
public interface ISysOssService {

    /**
     * 查询OSS对象存储列表
     *
     * @param sysOss    OSS对象存储分页查询对象
     * @param pageQuery 分页查询实体类
     * @return 结果
     */
    TableDataInfo<SysOssVo> queryPageList(SysOssBo sysOss, PageQuery pageQuery);

    /**
     * 根据一组 ossIds 获取对应的 SysOssVo 列表
     *
     * @param ossIds 一组文件在数据库中的唯一标识集合
     * @return 包含 SysOssVo 对象的列表
     */
    List<SysOssVo> listByIds(Collection<Long> ossIds);

    /**
     * 根据 ossId 从缓存或数据库中获取 SysOssVo 对象
     *
     * @param ossId 文件在数据库中的唯一标识
     * @return SysOssVo 对象，包含文件信息
     */
    SysOssVo getById(Long ossId);

    /**
     * 上传 MultipartFile 到对象存储服务，并保存文件信息到数据库
     *
     * @param file 要上传的 MultipartFile 对象
     * @return 上传成功后的 SysOssVo 对象，包含文件信息
     */
    SysOssVo upload(MultipartFile file, String uid) throws IOException;

    /**
     * 文件下载方法，支持一次性下载完整文件
     *
     * @param ossId    OSS对象ID
     * @param response HttpServletResponse对象，用于设置响应头和向客户端发送文件内容
     */
    void download(Long ossId, HttpServletResponse response,String uid) throws IOException;

    /**
     * 文件下载，文件不是从影像上传的，没有oss信息
     */
    void download(String fileId, HttpServletResponse response,String uid) throws IOException;

    /**
     * 文件下载
     *
     * @param ossId OSS对象ID
     * @throws IOException 抛出IO异常
     */
    byte[] downloadByte(Long ossId,String uid) throws IOException;

    /**
     * @param fileIds  [id1,id2,id3]
     * @param response
     * @throws IOException
     */
    void downloadByString(List<String> fileIds, HttpServletResponse response,String uid) throws IOException;

    /**
     * 删除OSS对象存储
     *
     * @param ids     OSS对象ID串
     * @param isValid 判断是否需要校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 上传 MultipartFile 到对象存储服务，并保存文件信息到数据库
     *
     * @param fileBytes        要上传的 字节数组
     * @param originalfileName 要上传的 文件名
     * @return 上传成功后的 SysOssVo 对象，包含文件信息
     */

    SysOssVo upload(byte[] fileBytes, String originalfileName, String uid) throws IOException;

    /**
     * 删除OSS对象存储
     *
     * @param id      OSS对象ID
     * @param isValid 判断是否需要校验
     * @return 结果
     */
    Boolean deleteWithValidById(Long id, Boolean isValid);
}
