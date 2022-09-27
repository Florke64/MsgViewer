package at.redeye.FrameWork.utilities.zip;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip
{

    public static void zip(File file_or_dir, File zip_name, ProgressListener listener) throws IOException {
        ProgressContainer container = null;

        if (listener != null)
            container = new ProgressContainer(listener);

        zip(file_or_dir, zip_name, container);
    }

    private static void zip(File file_or_dir, File zip_name, ProgressContainer progress_container) throws IOException {
        ZipOutputStream z = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zip_name.getAbsolutePath())));

        z.setLevel(9);

        if (progress_container != null)
            progress_container.init(file_or_dir);

        zip(file_or_dir, file_or_dir.getPath(), z, progress_container);

        z.close();
    }

    private static void zip(File file_or_dir, String path, ZipOutputStream z, ProgressContainer progress_container) throws IOException {
        if (file_or_dir.isDirectory()) {
            File[] files = file_or_dir.listFiles();

            for (File file : files) {
                zip(file, path, z, progress_container);
            }
            return;
        }

        byte[] readBuffer = new byte[4*1024];

        FileInputStream in = new FileInputStream( file_or_dir );

        String name = file_or_dir.getPath().substring(path.length());

        if( name.startsWith("/") )
            name = name.substring(1);

        name = name.replace('/', '\\');

        ZipEntry entry = new ZipEntry(name);

        z.putNextEntry(entry);

        int bytesIn;
        while ((bytesIn = in.read(readBuffer)) != -1)
        {
            z.write(readBuffer, 0, bytesIn);

            if( progress_container != null )
                progress_container.incProgress(bytesIn, 0);
        }

        in.close();

        if( progress_container != null )
            progress_container.incProgress(0, 1);
    }
}
