package services.signing;

import java.io.File;
import java.io.IOException;

public interface ISigningService 
{
	public void sign(File file) throws IOException;
}
