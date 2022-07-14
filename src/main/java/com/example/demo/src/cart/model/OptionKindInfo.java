package com.example.demo.src.cart.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionKindInfo {
    private int optionKindId;
    private Boolean isEssential;
    private List<OptionInfo> optionInfoList;
}
