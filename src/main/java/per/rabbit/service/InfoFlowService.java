package per.rabbit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.kotlin.AbstractKtWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import per.rabbit.dao.InfoFlowBean;
import per.rabbit.dao.InfoFlowMapper;

import java.util.List;

@Service
public class InfoFlowService {
    @Autowired
    private InfoFlowMapper infoFlowMapper;
    private static final Logger logger = LoggerFactory.getLogger(InfoFlowService.class);

    /**
     * 添加卡片数据
     *
     * @param infoFlowBean
     */
    public void addInfoFlow(InfoFlowBean infoFlowBean) {
        logger.info("add card data: {}", infoFlowBean);
        infoFlowMapper.insert(infoFlowBean);
    }

    /**
     * 获取卡片列表数据
     * @param page
     * @param size
     */
    public Page<InfoFlowBean> getInfoFlowList(int page, int size) {
        logger.info("get card data: {}, {}", page, size);

        return infoFlowMapper.selectPage(
                new Page<>(page, size),
                new QueryWrapper<>() {{
                    select("id, title, content, images, type");
                    orderByDesc("id");
                }});
    }
}
