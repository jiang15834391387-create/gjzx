package org.smartlink.server.nc.service.hardwaremessage.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import de.schlichtherle.license.LicenseContent;
import lombok.RequiredArgsConstructor;
import org.smartlink.common.core.domain.R;
import org.smartlink.server.nc.domain.hardwaremessage.HardWareMessage;
import org.smartlink.server.nc.license.modle.HardwareMessageBody;
import org.smartlink.server.nc.license.parm.LicenseVerifyParam;
import org.smartlink.server.nc.mapper.HardWareMessageMapper;
import org.smartlink.server.nc.service.hardwaremessage.HardWareMessageService;
import org.smartlink.server.nc.utils.LicenseVerifyInstallUtils;
import org.smartlink.server.nc.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HardWareMessageServiceImpl implements HardWareMessageService {

    private final HardWareMessageMapper baseMapper;
    @Autowired
    private final ConfigurableApplicationContext context;

    @Override
    public HardWareMessage whetherToAuthorize(String macIp) throws Exception {
        HardWareMessage hwm = this.selectMessage();
        if (ObjectUtil.isNull(hwm)) {
            //读取或刷新证书信息
            LicenseVerifyInstallUtils utils = new LicenseVerifyInstallUtils();
            LicenseVerifyParam licenseVerifyParam = new LicenseVerifyParam();
            LicenseContent message = utils.getMessage(licenseVerifyParam);
            HardwareMessageBody hardwareMessageBody = (HardwareMessageBody) message.getExtra();
            hwm = new HardWareMessage();
            hwm.setMaxCheckNum(Integer.parseInt(hardwareMessageBody.getMaxCheck()));
            hwm.setMaxOcrNum(Integer.parseInt(hardwareMessageBody.getMaxOcr()));
            hwm.setMacSumNum(Integer.parseInt(hardwareMessageBody.getMacSum()));
            hwm.setMiniSumNum(Integer.parseInt(hardwareMessageBody.getMiniSum()));
            hwm.setMainBoardSerial(hardwareMessageBody.getMainBoardSerial());
            hwm.setIsWaiOcr(hardwareMessageBody.getIsWaiOcr());
            hwm.setDelDate(hardwareMessageBody.getDelDate());
            hwm.setFileMd5(DigestUtils.md5DigestAsHex(Files.newInputStream(Paths.get(licenseVerifyParam.getLicensePath()))));
            baseMapper.insert(hwm);
        }
        int compare = DateUtil.compare(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(hwm.getDelDate()), new Date());
        if (compare <= 0) {
            ThreadUtil.execAsync(()->{
                ThreadUtil.sleep(10000);
                context.close();});

            hwm.setResult(1);
            hwm.setRcontent("授权过期！10秒后将关闭系统！");
            return hwm;
        }

        /**
         *判断是否还有ocr和查验次数(公网环境下)
         * */
        if (hwm.getIsWaiOcr() == 1) {
            if (hwm.getMaxCheckNum() < hwm.getRealCheckNum() || hwm.getMaxCheckNum() == hwm.getRealCheckNum()) {
                hwm.setResult(1);
                hwm.setRcontent("查验次数不足");
            } else if (hwm.getMaxOcrNum() < hwm.getRealOcrNum() || hwm.getMaxOcrNum() == hwm.getRealOcrNum()) {
                hwm.setResult(1);
                hwm.setRcontent("ocr次数不足");
            }
        }

        /**
         * 判断是否还有扫描台授权数量
         * */
        R<Void> voidR = haveSurplusMacIpList(macIp, hwm);
        if (voidR.getCode()==R.FAIL) {
            hwm.setResult(1);
            hwm.setRcontent(voidR.getMsg());
            return hwm;
        }

        return hwm;
    }

    @Override
    public R<Void> haveSurplusMacIpList(String macIp, HardWareMessage hwm) {
        /*如果是新连接的扫描台，判断扫描台授权数量是否充足，充足则加进扫描台mac地址list中*/
        if (StringUtils.isNotEmpty(macIp)) {
            //扫描台列表
            List<String> macList = new ArrayList<>();
            if (hwm.getMacIpList() != null) {
                macList = new ArrayList<>(Arrays.asList(hwm.getMacIpList().split(",")));
            }
            // 判断mac是否存在表中
            if (macList.contains(macIp)) {
                return R.ok();
            }
            // 不在表中则重新判断点数是否足够
            if (macList.size() < hwm.getMacSumNum()) {
                macList.add(macIp);
                hwm.setMacIpList(String.join(",", macList));
                hwm.updateById();
                return R.ok();
            }else {
                return R.fail("扫描点数不足！请联系管理员！");
            }
        } else {
            return R.ok();
        }
    }

    @Override
    public HardWareMessage selectMessage()  {
        List<HardWareMessage> hardWareMessages = baseMapper.selectList();
        if (ObjectUtil.isNotEmpty(hardWareMessages)) {
            HardWareMessage hwm = hardWareMessages.get(0);
            if(hwm.getMaxOcrNum()-hwm.getRealOcrNum()>0){
                hwm.setRestOcrNum(hwm.getMaxOcrNum()-hwm.getRealOcrNum());
            }else{
                hwm.setRestOcrNum(0);
            }
            if(hwm.getMaxCheckNum()-hwm.getRealCheckNum()>0){
                hwm.setRestCheckNum(hwm.getMaxCheckNum()-hwm.getRealCheckNum());
            }else{
                hwm.setRestCheckNum(0);
            }
            return hwm;
        }else {
            return null;
        }
    }
}
