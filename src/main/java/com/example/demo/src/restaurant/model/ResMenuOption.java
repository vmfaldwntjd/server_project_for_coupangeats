package com.example.demo.src.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResMenuOption {
    private int optionKindId;
    private String optionKindName;
    private int optionKindMaxCount;
    private Boolean isEssential;
    private List<OptionInfo> optionInfoList;
}
