package it.caoxin.mapper;

import it.caoxin.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from pbs_user where id = #{id}")
    public User getById(@Param("id")long id);

    @Insert("insert into pbs_user(id,nickname,password) values (#{id},#{nickname}, #{password})")
    public int insert(User user);
}
