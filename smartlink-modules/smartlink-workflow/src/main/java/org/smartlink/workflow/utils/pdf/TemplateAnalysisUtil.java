package org.smartlink.workflow.utils.pdf;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.smartlink.system.domain.vo.SysOssVo;
import org.smartlink.system.service.ISysOssService;
import org.smartlink.workflow.common.enums.ResourcePathType;
import org.smartlink.workflow.domain.TemplateInfo;
import org.smartlink.workflow.domain.TemplateOssFile;
import org.smartlink.workflow.mapper.EcTemplateInfoMapper;
import org.smartlink.workflow.mapper.TemplateOssFileMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * @author 86158
 */
@RequiredArgsConstructor
public class TemplateAnalysisUtil {
    private final ISysOssService sysOssService;
    private final TemplateOssFileMapper templateOssFileMapper;
    private final EcTemplateInfoMapper templateInfoMapper;



    /**
     * 获取模板信息
     *
     * @return
     */
    public TemplateInfo getTemplateInfo(String templateCode) {
        List<TemplateInfo> ecTemplateInfos = templateInfoMapper.selectList(Wrappers.<TemplateInfo>lambdaQuery().eq(TemplateInfo::getTemplateCode, templateCode).eq(TemplateInfo::getDelFlag, 0).orderByDesc(TemplateInfo::getCreateTime));
        if (!CollectionUtils.isEmpty(ecTemplateInfos)){
            return ecTemplateInfos.get(0);
        }else {
            return null;
        }
    }
    /**
     * 获取pdf模板字段数据
     *
     * @param
     * @return
     */
    public List<String> getPdfTemplateFileName(TemplateInfo voucherPdf) throws IOException {
        if (voucherPdf != null) {
            String templateStoragePath = voucherPdf.getTemplateStoragePath();
            String templateType = voucherPdf.getTemplateType();
            if (!StringUtils.isEmpty(templateStoragePath) && !StringUtils.isEmpty(templateType)) {
                ResourcePathType type = ResourcePathType.fromValue(templateType);
                List<String> stringList = PdfProcessUtil.extractPdfFieldNames(templateStoragePath, type);
                if (!CollectionUtils.isEmpty(stringList)) {
                    return stringList;
                }
            }
        }
        return null;
    }

    /**
     * 上传PDF文件
     * @param bytes
     * @param fileName  {@link }
     * @return
     * @throws Exception
     */
    public SysOssVo voucherAssembly( byte[] bytes, String fileName) throws Exception {
        SysOssVo upload = sysOssService.upload(bytes, fileName+ResourcePathType.PDF_SUFFIX.getValue(), ResourcePathType.CONTENT_TYPE_PDF.getValue());
        return upload;
    }

    /**
     * 解析模板文件
     * @param url
     * @param fillMap
     * @return
     * @throws Exception
     */
    public byte[] assemblyTemplate(String  url,Map<String, Object> fillMap) throws Exception {
        return PdfProcessUtil.generatePdf(url, ResourcePathType.URL, fillMap);
    }

    /**
     * 汇总模板字段
     *
     * @return
     */
    public Map<String, Object> templateFileFactory( Map<String, Object> pdfFileMap) {


        return pdfFileMap;
    }

    /**
     * 添加附件
     * @param workflowId
     * @param ossVo
     */
    public void addPdfAttachments(Long workflowId, SysOssVo ossVo){
        TemplateOssFile archiveAttachment = new TemplateOssFile();
        archiveAttachment.setWorkFlowId(workflowId);
        archiveAttachment.setFileId(String.valueOf(ossVo.getOssId()));
        archiveAttachment.setFileName(ossVo.getFileName());
        archiveAttachment.setFileUrl(ossVo.getFileName());
        archiveAttachment.setFileType(ResourcePathType.CONTENT_TYPE_PDF.getValue());
        archiveAttachment.setCreateTime(new Date());
        templateOssFileMapper.insert(archiveAttachment);
    }

}
