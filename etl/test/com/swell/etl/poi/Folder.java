package com.swell.etl.poi;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class Folder {

	public static void main(String[] args) {

//		String pathname = "C:/Users/Administrator/Desktop/建行模板";
//		String name = "利率补录模板_V0808.xlsx";
		
		
		System.out.println("usage: Folder pathname name");
		
		String pathname = args[0];
		String name = args[1];
		
		File directory = new File(pathname);
		IOFileFilter fileFilter = FileFilterUtils.nameFileFilter(name,
				IOCase.INSENSITIVE);
//		IOFileFilter dirFilter = FileFilterUtils.trueFileFilter();
		IOFileFilter dirFilter = TrueFileFilter.TRUE;
		Collection<File> result = FileUtils.listFiles(directory, fileFilter, dirFilter);
		
		System.out.println("============start");
		for (Iterator<File> iterator = result.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			System.out.println(file.getAbsolutePath());
		}

	}

}
