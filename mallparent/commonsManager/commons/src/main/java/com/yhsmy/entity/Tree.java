package com.yhsmy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @auth 李正义
 * @date 2019/11/6 22:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tree implements Serializable {

    private static final long serialVersionUID = 4064460980181905582L;
    /**
     * 层级数
     */
    private int layer;

    private String id;

    private String name;

    private String pid;

    /**
     * 是否开启，默认开启
     */
    private boolean open = Boolean.TRUE;

    /**
     * 是否选择，默认未选择
     */
    private boolean checked = Boolean.FALSE;

    /**
     * 子节点
     */
    private List<Tree> children = new ArrayList<Tree> (0);

    public Tree (int layer, String id, String name, String pid, boolean open, boolean checked) {
        this.layer = layer;
        this.id = id;
        this.name = name;
        this.pid = pid;
        this.open = open;
        this.checked = checked;
    }
}
