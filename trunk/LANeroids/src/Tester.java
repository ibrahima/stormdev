public class Tester{
	public static void main(String Args[]){
		//Creates a server and n clients
		new Server(12345);
		int n=2;
		new Client("127.0.0.1",true,"Ibrahim");
		for(int i=1;i<n;i++)
			new Client("127.0.0.1",false);
	}
}