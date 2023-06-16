package com.duyi.readingweb.entity.framework;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Newbanner {
    @TableId
    private Integer newsbannerid;

    private String url;

    private String imgsrc;

    private Integer showednum;
    private Integer line;

}