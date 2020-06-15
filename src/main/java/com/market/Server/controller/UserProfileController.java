package com.market.Server.controller;

import com.market.Server.advice.exception.CUserNotFoundException;
import com.market.Server.mapper.UserProfileMapper;
import com.market.Server.model.response.CommonResult;
import com.market.Server.model.response.SingleResult;
import com.market.Server.dto.UserDTO;
import com.market.Server.service.ResponseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Api(tags = {"1. users"})
@RequiredArgsConstructor
@RestController
public class UserProfileController {

    private final UserProfileMapper  mapper;
    private final ResponseService responseService;

    @ApiOperation(value = "회원 한명 조회", notes = "특정 회원을 조회한다")
    @GetMapping("/user/{id}")
    public SingleResult<UserDTO> getUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable ("id") String id) throws Exception {
        return responseService.getSingleResult(Optional.ofNullable(mapper.getUserProfile(id)).orElseThrow(CUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 단건 조회", notes = "userId로 회원을 조회한다")
    @GetMapping(value = "/usermsg/{msrl}")
    public SingleResult<UserDTO> findUserById(@ApiParam(value = "회원ID", required = true) @PathVariable ("msrl") String msrl, @ApiParam(value = "언어", defaultValue = "ko") @RequestParam String lang) {
        // 결과데이터가 단일건인경우 getBasicResult를 이용해서 결과를 출력한다.
        return responseService.getSingleResult(Optional.ofNullable(mapper.getUserProfile(msrl)).orElseThrow(CUserNotFoundException::new));
    }

    @ApiOperation(value = "회원 수정", notes = "회원을 수정한다..")
    @PutMapping("/user/{id}")
    public SingleResult<Integer> putUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id,
                                                @ApiParam(value = "회원비번", required = true) @RequestParam("pw") String pw,
                                                @ApiParam(value = "회원이름", required = true) @RequestParam("name") String name,
                                                @ApiParam(value = "회원폰번호", required = true) @RequestParam("phone") String phone,
                                                @ApiParam(value = "회원주소", required = true) @RequestParam("address") String address) {
       return responseService.getSingleResult(mapper.updateUserProfile(id,pw,name,phone,address));
    }

    @ApiOperation(value = "회원 입력", notes = "회원을 입력한다..")
    @PostMapping("/user/{id}")
    public SingleResult<Integer> postUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id,
                                                 @ApiParam(value = "회원비번", required = true) @RequestParam("pw") String pw,
                                                 @ApiParam(value = "회원이름", required = true) @RequestParam("name") String name,
                                                 @ApiParam(value = "회원폰번호", required = true) @RequestParam("phone") String phone,
                                                 @ApiParam(value = "회원주소", required = true) @RequestParam("address") String address)  {
        return responseService.getSingleResult(mapper.insertUserProfile(id,pw,name,phone,address));
    }

    @DeleteMapping("/user/{id}")
    public CommonResult deleteUserProfile(@ApiParam(value = "회원아이디", required = true) @PathVariable("id") String id) {
        mapper.deleteUserProfile(id);
        // 성공 결과 정보만 필요한경우 getSuccessResult()를 이용하여 결과를 출력한다.
        return responseService.getSuccessResult();
    }

}
