package com.benson.swagger.api.entity.postman;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
public class PostmanItem {
    private List<String> name;
    private List<PostmanMethod> item;

    public PostmanItem addName(String nameResult) {
        name = Optional.ofNullable(name).orElse(new ArrayList<>());
        name.add(nameResult);
        return this;
    }

    public PostmanItem addItem(PostmanMethod itemResult) {
        item = Optional.ofNullable(item).orElse(new ArrayList<>());
        item.add(itemResult);
        return this;
    }
}
