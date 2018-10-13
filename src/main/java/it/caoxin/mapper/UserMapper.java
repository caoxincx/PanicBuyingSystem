package it.caoxin.mapper;

import it.caoxin.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from pbs_user where id = #{id}")
     User getById(@Param("id")long id);

    @Insert("insert into pbs_user(id,nickname,password,login_count,register_date,salt) values (#{id},#{nickname},#{password},#{loginCount},#{registerDate},#{salt})")
     int insert(User user);
}
