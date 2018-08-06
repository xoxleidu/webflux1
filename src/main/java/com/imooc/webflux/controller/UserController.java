package com.imooc.webflux.controller;

import com.imooc.webflux.domain.User;
import com.imooc.webflux.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@RestController
@RequestMapping("")
@Api(tags = "USER",description = "用户相关")
public class UserController {

    /**
     * 注入仓库并构造函数：官方推荐
     * 相当于@Autoward但过时了
     * @param repository
     */
    private final UserRepository repository;
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @ApiOperation(value = "FLUX查询所有数据",notes = "一次性返回参数")
    @GetMapping("/all")
    public Flux<User> getAll(){
        return repository.findAll();
    }

    @ApiOperation(value = "FLUX查询所有数据",notes = "数据流返回参数")
    @GetMapping(value = "/all/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamGetAll(){
        return repository.findAll();
    }

    @ApiOperation(value = "添加用户",notes = "有id是修改，id为空是新增，已经置空id，当前时间new Date()")
    @PostMapping("/add")
    public Mono<User> createUser(
            @RequestBody User user
    ){
        //spring data jpa 里面，新增和修改都是save，有id是修改，id为空是新增
        //根据实际情况是否置空id
        user.setId(null);
        user.setDate(new Date());
        return this.repository.save(user);
    }

    @ApiOperation(value = "根据ID删除用户",notes = "存在返回200，不存在返回404")
    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(
            @PathVariable("id") String id
    ){
        //deletebyID 没有返回值，不能判断数据是否存在
        //this.repository.deleteById(id);
        return this.repository.findById(id)
                //操作数据，返回MONO，用FLATMAP
                //不操作数据，只是转换数据，用MAP
                .flatMap(user -> this.repository.delete(user)
                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "根据ID修改用户数据",notes = "存在返回200和修改后的数据，不存在返回404")
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<User>> updateUser(
            @PathVariable("id") String id,
            @RequestBody User user
    ){
        return this.repository.findById(id)
                //flatMap 操作数据
                .flatMap(u -> {
                    u.setAge(user.getAge());
                    u.setName(user.getName());
                    u.setPassword(user.getPassword());
                    return this.repository.save(u);
                })
                //map 转换数据
                .map(u -> new ResponseEntity<User>(u,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "根据ID查找用户",notes = "存在返回200和用户数据，不存在返回404")
    @GetMapping("/find/{id}")
    public Mono<ResponseEntity<User>> findUserById(
            @PathVariable("id") String id
    ){
        return this.repository.findById(id)
                .map(u -> new ResponseEntity<User>(u,HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "FLUX根据年龄段查找用户",notes = "一次性返回参数")
    @GetMapping("/find/age/{start}/{end}")
    public Flux<User> findByAge(
            @PathVariable("start") int start,
            @PathVariable("end") int end
    ){
        return this.repository.findByAgeBetween(start,end);
    }

    @ApiOperation(value = "FLUX根据年龄段查找用户",notes = "数据流返回参数")
    @GetMapping(value = "/find/age/stream/{start}/{end}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<User> streamFindByAge(
            @PathVariable("start") int start,
            @PathVariable("end") int end
    ){
        return this.repository.findByAgeBetween(start,end);
    }


}
