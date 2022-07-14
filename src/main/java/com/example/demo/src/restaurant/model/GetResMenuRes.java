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
public class GetResMenuRes {
    private ResMenuInfo resMenuInfo;
    private List<ResMenuOption> resMenuOptionList;
}
