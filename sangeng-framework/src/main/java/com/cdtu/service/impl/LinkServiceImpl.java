package com.cdtu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cdtu.constants.SystemConstants;
import com.cdtu.domain.ResponseResult;
import com.cdtu.domain.dto.AddLinkDto;
import com.cdtu.domain.dto.ListLinkDto;
import com.cdtu.domain.dto.ModifyLinkDto;
import com.cdtu.domain.entity.Article;
import com.cdtu.domain.entity.Link;
import com.cdtu.domain.vo.link.LinkVo;
import com.cdtu.domain.vo.PageVo;
import com.cdtu.enums.AppHttpCodeEnum;
import com.cdtu.exception.SystemException;
import com.cdtu.mapper.LinkMapper;
import com.cdtu.service.LinkService;
import com.cdtu.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author mika
 * @since 2023-05-12 23:05:02
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

    @Override
    public ResponseResult<?> getAllLink() {
        // 查询所有审核通过的友链
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);

        List<Link> links = list(queryWrapper);
        // 转换为 vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        // 封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<?> listLink(Integer pageNum, Integer pageSize, ListLinkDto listLinkDto) {
        String name = listLinkDto.getName();
        String status = listLinkDto.getStatus();

        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Link::getName, name);
        queryWrapper.eq(StringUtils.hasText(status), Link::getStatus, status);
        // 分页查询
        Page<Link> curPage = new Page<>(pageNum, pageSize);
        page(curPage, queryWrapper);

        // 封装查询结果
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(curPage.getRecords(), LinkVo.class);
        PageVo<LinkVo> pageVo = new PageVo<>(linkVos, curPage.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult<?> addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> getLinkById(Long id) {
        if (!linkExist(id)) throw new SystemException(AppHttpCodeEnum.LINK_NOT_EXIST);
        Link link = getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(link, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult<?> modifyLink(ModifyLinkDto modifyLinkDto) {
        Long id = modifyLinkDto.getId();
        if (!linkExist(id)) throw new SystemException(AppHttpCodeEnum.LINK_NOT_EXIST);
        Link link = BeanCopyUtils.copyBean(modifyLinkDto, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<?> removeLink(Long id) {
        if (!linkExist(id)) throw new SystemException(AppHttpCodeEnum.LINK_NOT_EXIST);
        removeById(id);
        return ResponseResult.okResult();
    }

    private boolean linkExist(Long id) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getId, id);
        return count(queryWrapper) > 0;
    }
}
