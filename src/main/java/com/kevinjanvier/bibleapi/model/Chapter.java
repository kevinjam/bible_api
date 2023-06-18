package com.kevinjanvier.bibleapi.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Chapter {
    private int number;
    private List<Verse> verses;
}
