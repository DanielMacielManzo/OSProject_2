Descriptor I used:
protected OpenFile[] myFileSlots;
myFileSlots = new OpenFile[16]; 
myFileSlots[0] = UserKernel.console.openForReading();  
myFileSlots[1] = UserKernel.console.openForWriting();

private int handleOpen(int argument0)
    {
    	//String name = "testing";
    	String filename;
    	filename = readVirtualMemoryString(argument0,256);
    	OpenFile file = ThreadedKernel.fileSystem.open(filename,false);
    	if(file == null)
    	{
    		return -1; //file can not be open
    	}
    	else
    	{
    		for(int i = 0;i<16;i++)
    		{
    			if(myFileSlots[i]==null)
    			{
    				myFileSlots[i] = file;//put to file descriptor
    				return i;
    			}
    		}
    		//return -1;
    	}
    	return -1;
    	//OpenFile executable = ThreadedKernel.fileSystem.open(name, false);
    	//return argument0;
    }

    //argumnent0 file descriptor, argument1 buffer size, argument2 count
    private int handleRead(int argument0, int argument1, int argument2) {
		//look file OpenFile
		OpenFile file = myFileSlots[argument0];
		if (file == null) {
			return -1;
		}

		//create byte buffer size of content
		byte buffer[] = new byte[argument2];
		int byteReadC = file.read(buffer, 0, argument2);
		if (byteReadC == -1) {
			System.out.println("Exeption: File Cannot Be Read");
			return -1;
		}
		//store what was read into the virtual memory
		int exit = writeVirtualMemory(argument1, buffer, 0, byteReadC);
		return exit;
	}

	private int handleWrite(int argument0, int argument1, int argument2) {
		//look file OpenFile
		OpenFile file = myFileSlots[argument0];
		if (file == null) {
			return -1;
		}
		//create byte buffer size of content
		byte[] buffer = new byte[argument2];

		// read data from argument1 to buffer
		int read = readVirtualMemory(argument1, buffer, 0, argument2);

		//write the bytes from the buffer
		int write = file.write(buffer, 0, w);

		if (write == -1 || read == -1) {
			System.out.println("Exception: Cannot Write to File");
		}

        //check if we wrote everything otherwise throw exep
		if (write < argument2) {
			return -1;
		}
		return write;
	}

