package per.rabbit.common;

import com.alibaba.fastjson2.JSONArray;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(StringListTypeHandler.class);
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.clear();
        jsonArray.addAll(parameter);
        ps.setString(i, jsonArray.toJSONString());
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        logger.info("getNullableResult: {}, {}", rs.getString(columnName), rs.getString(columnName));
        return JSONArray.parseArray(rs.getString(columnName), String.class);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        logger.info("getNullableResult: {}", rs.getString(columnIndex));
        return JSONArray.parseArray(rs.getString(columnIndex), String.class);
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        logger.info("getNullableResult: {}", cs.getString(columnIndex));
        return JSONArray.parseArray(cs.getString(columnIndex), String.class);
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        logger.info("getResult: {}", cs.getString(columnIndex));
        return super.getResult(cs, columnIndex);
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        logger.info("getResult: {}", rs.getString(columnIndex));
        return super.getResult(rs, columnIndex);
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        logger.info("getResult: {}", rs.getString(columnName));
        return super.getResult(rs, columnName);
    }
}
