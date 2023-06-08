package com.cdtu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddLinkDto;
import com.cdtu.domain.dto.ListLinkDto;
import com.cdtu.domain.dto.ModifyLinkDto;
import com.cdtu.domain.entity.Link;


/**
 * 友链(Link)表服务接口
 *
 * @author mika
 * @since 2023-05-12 23:05:02
 */
public interface LinkService extends IService<Link> {

    ResponseResult<?> getAllLink();

    ResponseResult<?> listLink(Integer pageNum, Integer pageSize, ListLinkDto listLinkDto);

    ResponseResult<?> addLink(AddLinkDto addLinkDto);

    ResponseResult<?> getLinkById(Long id);

    ResponseResult<?> modifyLink(ModifyLinkDto modifyLinkDto);

    ResponseResult<?> removeLink(Long id);
}
