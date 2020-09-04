package net.gudenau.minecraft.asm.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.Queue;

public class FileUtils{
    public static void delete(Path root) throws IOException{
        Queue<Path> dirs = new LinkedList<>();
        Queue<Path> files = new LinkedList<>();
        Queue<Path> remaining = new LinkedList<>();
        remaining.add(root);
        while(!remaining.isEmpty()){
            Path current = remaining.poll();
            if(Files.isDirectory(current)){
                Files.list(current).forEach((p)->{
                    if(Files.isDirectory(p)){
                        remaining.add(p);
                    }else{
                        files.add(p);
                    }
                });
                dirs.add(current);
            }else{
                files.add(current);
            }
        }
        for(Path file : files){
            Files.deleteIfExists(file);
        }
        for(Path dir : dirs){
            Files.deleteIfExists(dir);
        }
    }
}
