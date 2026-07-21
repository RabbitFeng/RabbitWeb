package per.rabbit.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import per.rabbit.common.AbsLruCache;
import per.rabbit.dao.FileDao;
import per.rabbit.dao.FileMapper;

import java.util.HashMap;
import java.util.List;

@Component
public class FileCache extends AbsLruCache<FileDao> {
    private static final Logger log = LoggerFactory.getLogger(FileCache.class);

    @Autowired
    private FileMapper fileMapper;

    @Transactional(rollbackFor = Exception.class, timeout = 5)
    @Override
    public void put(String key, FileDao value) {
        log.info("put: {} {}", key, value);
        super.put(key, value);

        // TODO 持久化存储
        fileMapper.insert(value);
    }

    @Transactional(rollbackFor = Exception.class, timeout = 5)
    @Override
    protected FileDao loadData(String key) {
        log.info("loadData: {}", key);
        List<FileDao> uuidList = fileMapper.selectByMap(new HashMap<>() {{
            put("mapped_name", key);
        }});
        if (uuidList.isEmpty()) {
            log.warn("file not found: {}", key);
            return null;
        }
        if (uuidList.size() != 1) {
            log.warn("multiple files found: {} {}", key, uuidList);
        }
        return uuidList.get(0);
    }
}
