package org.smartlink.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.smartlink.common.core.constant.CacheNames;
import org.smartlink.common.core.domain.dto.OssDTO;
import org.smartlink.common.core.exception.ServiceException;
import org.smartlink.common.core.service.OssService;
import org.smartlink.common.core.utils.MapstructUtils;
import org.smartlink.common.core.utils.SpringUtils;
import org.smartlink.common.core.utils.StreamUtils;
import org.smartlink.common.core.utils.StringUtils;
import org.smartlink.common.core.utils.file.FileUtils;
import org.smartlink.common.mybatis.core.page.PageQuery;
import org.smartlink.common.mybatis.core.page.TableDataInfo;
import org.smartlink.common.oss.constant.OssConstant;
import org.smartlink.common.oss.core.OssClient;
import org.smartlink.common.oss.entity.UploadResult;
import org.smartlink.common.oss.enumd.AccessPolicyType;
import org.smartlink.common.oss.factory.MultiFileSysFactory;
import org.smartlink.common.oss.factory.OssFactory;
import org.smartlink.common.oss.service.StrategyService;
import org.smartlink.common.redis.utils.RedisUtils;
import org.smartlink.system.domain.SysOss;
import org.smartlink.system.domain.bo.SysOssBo;
import org.smartlink.system.domain.vo.SysOssVo;
import org.smartlink.system.mapper.SysOssMapper;
import org.smartlink.system.service.ISysOssService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 文件上传 服务层实现
 *
 * @author Lion Li
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysOssServiceImpl implements ISysOssService, OssService {

    private final SysOssMapper baseMapper;
    private final MultiFileSysFactory multiFileSysFactory;
    private final StrategyService strategyService;


    /**
     * 查询OSS对象存储列表
     *
     * @param bo        OSS对象存储分页查询对象
     * @param pageQuery 分页查询实体类
     * @return 结果
     */
    @Override
    public TableDataInfo<SysOssVo> queryPageList(SysOssBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysOssVo> filterResult = StreamUtils.toList(result.getRecords(), this::matchingUrl);
        result.setRecords(filterResult);
        return TableDataInfo.build(result);
    }

    /**
     * 根据一组 ossIds 获取对应的 SysOssVo 列表
     *
     * @param ossIds 一组文件在数据库中的唯一标识集合
     * @return 包含 SysOssVo 对象的列表
     */
    @Override
    public List<SysOssVo> listByIds(Collection<Long> ossIds) {
        List<SysOssVo> list = new ArrayList<>();
        for (Long id : ossIds) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    list.add(this.matchingUrl(vo));
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(vo);
                }
            }
        }
        return list;
    }

    /**
     * 根据一组 ossIds 获取对应文件的 URL 列表
     *
     * @param ossIds 以逗号分隔的 ossId 字符串
     * @return 以逗号分隔的文件 URL 字符串
     */
    @Override
    public String selectUrlByIds(String ossIds) {
        List<String> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(ossIds, Convert::toLong)) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    list.add(this.matchingUrl(vo).getUrl());
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(vo.getUrl());
                }
            }
        }
        return String.join(StringUtils.SEPARATOR, list);
    }

    @Override
    public List<OssDTO> selectByIds(String ossIds) {
        List<OssDTO> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(ossIds, Convert::toLong)) {
            SysOssVo vo = SpringUtils.getAopProxy(this).getById(id);
            if (ObjectUtil.isNotNull(vo)) {
                try {
                    vo.setUrl(this.matchingUrl(vo).getUrl());
                    list.add(BeanUtil.toBean(vo, OssDTO.class));
                } catch (Exception ignored) {
                    // 如果oss异常无法连接则将数据直接返回
                    list.add(BeanUtil.toBean(vo, OssDTO.class));
                }
            }
        }
        return list;
    }

    private LambdaQueryWrapper<SysOss> buildQueryWrapper(SysOssBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.like(StringUtils.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StringUtils.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(ObjectUtil.isNotNull(bo.getCreateBy()), SysOss::getCreateBy, bo.getCreateBy());
        lqw.eq(StringUtils.isNotBlank(bo.getService()), SysOss::getService, bo.getService());
        lqw.orderByAsc(SysOss::getOssId);
        return lqw;
    }

    /**
     * 根据 ossId 从缓存或数据库中获取 SysOssVo 对象
     *
     * @param ossId 文件在数据库中的唯一标识
     * @return SysOssVo 对象，包含文件信息
     */
    @Cacheable(cacheNames = CacheNames.SYS_OSS, key = "#ossId")
    @Override
    public SysOssVo getById(Long ossId) {
        return baseMapper.selectVoById(ossId);
    }


    /**
     * 文件下载方法，支持一次性下载完整文件
     *
     * @param ossId    OSS对象ID
     * @param response HttpServletResponse对象，用于设置响应头和向客户端发送文件内容
     */
    @Override
    public void download(Long ossId, HttpServletResponse response, String uid) throws IOException {
        SysOssVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        FileUtils.setAttachmentResponseHeader(response, sysOss.getOriginalName());
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");

        String serviceName = sysOss.getService();
        String fileName = sysOss.getFileName();
        //这个ossId是保存时在自动生成的，不是wenjian
        long contentLength = multiFileSysFactory.download(sysOss.getFileId(), serviceName, fileName, response, uid);

        response.setContentLengthLong(contentLength);
    }

    @Override
    public void download(String fileId, HttpServletResponse response, String uid) throws IOException {
        String fileName = RedisUtils.getCacheObject(OssConstant.RUN_JIAN_TOKEN_KEY+fileId);
        log.info("文件下载获取文件名：{},UID:{}", fileName, uid);
        FileUtils.setAttachmentResponseHeader(response, fileName);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8");
        //这个ossId是保存时在自动生成的，不是wenjian
        long contentLength = multiFileSysFactory.download(fileId, "runjian", fileName, response, uid);

        response.setContentLengthLong(contentLength);
    }

    @Override
    public byte[] downloadByte(Long ossId, String uid) {
        SysOssVo sysOss = SpringUtils.getAopProxy(this).getById(ossId);
        if (ObjectUtil.isNull(sysOss)) {
            throw new ServiceException("文件数据不存在!");
        }
        return multiFileSysFactory.downloadByte(sysOss.getFileId(), sysOss.getService(), sysOss.getFileName(), uid);
    }

    @Override
    public void downloadByString(List<String> fileIds, HttpServletResponse response, String uid) {
        String ossIdsString = fileIds.toString();
        //润建公司批量下载文件，直接调用他们的批量下载方法
        strategyService.download(ossIdsString, response, uid);

    }

    /**
     * 上传 MultipartFile 到对象存储服务，并保存文件信息到数据库
     *
     * @param file 要上传的 MultipartFile 对象
     * @return 上传成功后的 SysOssVo 对象，包含文件信息
     * @throws ServiceException 如果上传过程中发生异常，则抛出 ServiceException 异常
     */
    @Override
    public SysOssVo upload(MultipartFile file, String uid) throws IOException {
        String originalfileName = file.getOriginalFilename();
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        // 重写文件服务器工厂
        Map map = multiFileSysFactory.uploadSuffix(file, suffix, uid);
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, configKey, (UploadResult) map.get("uploadResult"), (String) map.get("id"));
    }


    @NotNull
    private SysOssVo buildResultEntity(String originalfileName, String suffix, String configKey, UploadResult uploadResult, String fileId) {
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(originalfileName);
        oss.setService(configKey);
        oss.setFileId(fileId);
        baseMapper.insert(oss);
        SysOssVo sysOssVo = MapstructUtils.convert(oss, SysOssVo.class);
        return this.matchingUrl(sysOssVo);
    }

    /**
     * 删除OSS对象存储
     *
     * @param ids     OSS对象ID串
     * @param isValid 判断是否需要校验
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysOss> list = baseMapper.selectBatchIds(ids);
        for (SysOss sysOss : list) {
            multiFileSysFactory.deleteWithValidByIds(sysOss.getService(), sysOss.getUrl());
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public SysOssVo upload(byte[] fileBytes, String originalfileName, String uid) {
        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        Map map = multiFileSysFactory.uploadSuffix(fileBytes, suffix, uid);
        String configKey = RedisUtils.getCacheObject(OssConstant.DEFAULT_CONFIG_KEY);
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, configKey, (UploadResult) map.get("uploadResult"), (String) map.get("id"));
    }

    /**
     * 桶类型为 private 的URL 修改为临时URL时长为120s
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private SysOssVo matchingUrl(SysOssVo oss) {
        if (OssConstant.RUN_JIAN_CONFIG_KEY.equals(oss.getService())) {
            return oss;
        } else {
            OssClient storage = OssFactory.instance(oss.getService());
            // 仅修改桶类型为 private 的URL，临时URL时长为120s
            if (AccessPolicyType.PRIVATE == storage.getAccessPolicy()) {
                oss.setUrl(storage.getPrivateUrl(oss.getFileName(), 120));
            }
            return oss;
        }
    }


    /**
     * 删除OSS对象存储
     *
     * @param id      OSS对象ID
     * @param isValid 判断是否需要校验
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidById(Long id, Boolean isValid) {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        SysOssVo sysOssVo = baseMapper.selectVoById(id);
        if (sysOssVo == null) {
            return true;
        }
        multiFileSysFactory.deleteWithValidById(sysOssVo.getService(), sysOssVo.getUrl());
        return baseMapper.deleteById(id) > 0;
    }
}
