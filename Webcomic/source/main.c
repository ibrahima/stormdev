// Load a simple background, very easy and simple...

// Includes
#include <PA9.h>       // Include for PA_Lib


// Converted using PAGfx
#include "gfx/all_gfx.c"
#include "gfx/all_gfx.h"

char text[200]; // This will be our text...

// Function: main()
int main(int argc, char ** argv)
{
	PA_Init();    // Initializes PA_Lib
	PA_InitVBL(); // Initializes a standard VBL
	
	// Load Backgrounds with their palettes !
	PA_EasyBgLoad(0, // screen
					3, // background number (0-3)
					menu); // Background name, used by PAGfx...
	
	//PA_EasyBgLoad(1, 0, menu);	
	PA_InitText(1, 0);  // Initialise the text system on the top screen

	
	u8 i;
	// Infinite loop to keep the program running
	while (1)
	{
		// Erase the text on the screen...
		for (i = 0; i < 10; i++) PA_OutputSimpleText(1, 6, 7+i, "                              ");
		
		// Check the stylus presses :
		if (Stylus.Held) PA_OutputSimpleText(1, 6, 7, "Stylus is held !");
		if (Stylus.Newpress) PA_OutputSimpleText(1, 6, 8, "Stylus is newly pressed !");
		if (Stylus.Released) PA_OutputSimpleText(1, 6, 9, "Stylus is just released !");
				
		// Get the stylus position and show it on screen
		PA_OutputText(1, 1, 11, "Stylus Position : %d, %d   ", Stylus.X, Stylus.Y);	
		PA_WaitForVBL();
	}
	
	return 0;
} // End of main()
