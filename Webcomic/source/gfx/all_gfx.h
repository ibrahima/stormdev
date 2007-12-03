//Gfx converted using Mollusk's PAGfx Converter

//This file contains all the .h, for easier inclusion in a project

#ifndef ALL_GFX_H
#define ALL_GFX_H

#ifndef PAGfx_struct
    typedef struct{
    void *Map;
    int MapSize;
    void *Tiles;
    int TileSize;
    void *Palette;
    int *Info;
} PAGfx_struct;
#endif


// Background files : 
extern const int menu_Info[3]; // BgMode, Width, Height
extern const unsigned short menu_Map[768] __attribute__ ((aligned (4))) ;  // Pal : menu_Pal
extern const unsigned char menu_Tiles[31616] __attribute__ ((aligned (4))) ;  // Pal : menu_Pal
extern PAGfx_struct menu; // background pointer


// Palette files : 
extern const unsigned short menu_Pal[214] __attribute__ ((aligned (4))) ;


#endif

