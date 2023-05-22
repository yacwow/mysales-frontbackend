package com.duyi.readingweb.entity.eventManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionEvent {
    @TableId(type = IdType.AUTO)
    private Integer promotionEventid;

    private String promotioncode;

    private String promotioncategory;

    private Integer percentdiscount;

    private Integer fixdiscount;
    private Boolean active;

}