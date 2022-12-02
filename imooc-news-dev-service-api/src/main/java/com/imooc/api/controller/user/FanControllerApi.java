package com.imooc.api.controller.user;


import com.imooc.utils.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

@RequestMapping("/fans")
@Api(value = "FanController")
public interface FanControllerApi {

    @PostMapping("/isMeFollowThisWriter")
    @ApiOperation(value = "if user follows writer", httpMethod = "POST")
    public GraceJSONResult isFollower(@RequestParam @NotBlank String writerId,
                                      @RequestParam @NotBlank String fanId);

    @PostMapping("/follow")
    @ApiOperation(value = "follow writer", httpMethod = "POST")
    public GraceJSONResult follow(@RequestParam @NotBlank String writerId,
                                  @RequestParam @NotBlank String fanId);

    @PostMapping("/unfollow")
    @ApiOperation(value = "unfollow writer", httpMethod = "POST")
    public GraceJSONResult unfollow(@RequestParam @NotBlank String writerId,
                                    @RequestParam @NotBlank String fanId);

    @PostMapping("/queryAll")
    @ApiOperation(value = "get fan list", httpMethod = "POST")
    public GraceJSONResult getFanList(@RequestParam String writerId,
                                      @ApiParam(required = false)
                                      @RequestParam Integer page,
                                      @ApiParam(required = false)
                                      @RequestParam Integer pageSize);

    @PostMapping("/queryRatio")
    @ApiOperation(value = "get the ratio of male to female", httpMethod = "POST")
    public GraceJSONResult getRatioOfSex(@RequestParam @NotBlank String writerId);

    @PostMapping("/queryRatioByRegion")
    @ApiOperation(value = "get the ratio by regions", httpMethod = "POST")
    public GraceJSONResult getRatioOfProvince(@RequestParam String writerId);

    @PostMapping("/sync")
    @ApiOperation(value = "sync fan/follow count to redis", httpMethod = "POST")
    public GraceJSONResult updateRedisFanFollowCount();

}
