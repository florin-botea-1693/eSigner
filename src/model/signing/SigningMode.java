package model.signing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface SigningMode {
	public void performSign(File file) throws FileNotFoundException, IOException;
}
