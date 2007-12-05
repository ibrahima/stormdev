// Load a simple background, very easy and simple...

// Includes
#include <PA9.h>       // Include for PA_Lib
#include <fat.h>

// Converted using PAGfx
#include "gfx/all_gfx.c"
#include "gfx/all_gfx.h"

char *text="No version checked"; // This will be our text...
char *conf="No configuration loaded"; // This will be our text...
int update();
const float version = 0.01;
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
	fatInitDefault(); //Initialise fat library


	FILE* testWrite = fopen ("webcomicds.cfg", "r+"); //w = create/truncate & write 
	if(testWrite==NULL){
		testWrite = fopen ("webcomicds.cfg", "w+"); //w = create/truncate & write 
	}
	fwrite("XKCD\nhttp://xkcd.com/atom.xml", 29, 1, testWrite);
	fclose(testWrite);
	
	// Infinite loop to keep the program running
	while (1)
	{
		// Erase the text on the screen...
		for (i = 0; i < 24; i++) PA_OutputSimpleText(1, 0, 0+i, "                                ");
		//Greet the user!
		PA_OutputText(1, 2, 4, "Hi, %s!", PA_UserInfo.Name);
		// Check the stylus presses :
		if (Stylus.Held) PA_OutputSimpleText(1, 0, 2, "Stylus is held !");
		if (Stylus.Newpress) PA_OutputSimpleText(1, 6, 8, "Stylus is newly pressed !");
		if (Stylus.Released) PA_OutputSimpleText(1, 6, 9, "Stylus is just released !");
		//check to see if stylus is on a button
		if (Stylus.Held && Stylus.X>10 && Stylus.X<81 && Stylus.Y>68 && Stylus.Y<127)
			PA_OutputSimpleText(1, 6, 10, "Vew Comics pressed!");
		if (Stylus.Held && Stylus.X>92 && Stylus.X<163 && Stylus.Y>68 && Stylus.Y<127)
			PA_OutputSimpleText(1, 6, 10, "Configure pressed!");
		if (Stylus.Held && Stylus.X>172 && Stylus.X<243 && Stylus.Y>68 && Stylus.Y<127)
		{
			int updateStatus = update();
			switch(updateStatus)
			{
		    case 1:
				PA_OutputSimpleText(1, 6, 10, "Updated Successfully!");
				break;
			case 0:
				PA_OutputSimpleText(1, 6, 10, "No new version.");
				break;
			case -1:
				PA_OutputSimpleText(1, 6, 10, "Update failed.");
				break;
			}
		}
		// Get the stylus position and show it on screen
		PA_OutputText(1, 0, 0, "%d, %d   ", Stylus.X, Stylus.Y);	
		PA_OutputSimpleText(1, 1, 12, text);	
		PA_WaitForVBL();
	}
	
	return 0;
} // End of main()
//Autoupdate function-returns 1 if successful update, 0 if no new update or -1 if failed.
int update(){
    PA_InitWifi(); //Initializes the wifi
    PA_ConnectWifiWFC();
	PA_OutputSimpleText(1, 1, 10, "Connected to Wifi");
	char *buffer = new char[256*256];
	PA_GetHTTP(buffer,"http://ib.freehostia.com/webcomicDS/version.txt");
	PA_OutputSimpleText(1, 1, 10, buffer);
	text=buffer;
	Wifi_DisconnectAP();//disables wifi
	Wifi_DisableWifi();
		PA_OutputSimpleText(1, 1, 10, "Disconnected from Wifi");
	return -1;
}
