/**
 * 
 */
package kocsist.blogic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import kocsist.model.Edge;
import kocsist.model.Node;
import kocsist.model.Picture;

/**
 * @author kocsist
 * 
 *  Required JDK 9 or above
 *  
 *  Mindenfele konvertalo, seged, egyeb algoritmus,
 *  ami mas projektekben is hasznos lehet. 
 *
 */
public final class MyHelper {
	public static final String emptydesctext = "#üres";
	public static final String abc = "aAbBcCdDeEfFgGhHiIjJkKlLmMnNoOpPqQrRsStTuUvVwWxXyYzZ";
	public static Random rnd = new Random();
	// source: https://www.baeldung.com/sha-256-Fhashing-java
	// (not thread-safe)G
	public static String encodeStringToHash(String original) {
		if(original != null && original.length() > 0) {
			MessageDigest md;
			final byte[] hashbytes;
			try {
				md = MessageDigest.getInstance("SHA3-256");
				hashbytes = md.digest(original.getBytes(StandardCharsets.UTF_8));
				return bytesToHex(hashbytes);
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalArgumentException(e);
			}
		}
		else
			return original;
	}
	
	public static boolean isSameHash(String txt1, String txt2) {
		return encodeStringToHash(txt1).equals(encodeStringToHash(txt2));
	}
	
	private static String bytesToHex(byte[] hash) {
		StringBuilder sb = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hexstr = Integer.toHexString(0xff & hash[i]);
		    if(hexstr.length() == 1) {
		    	sb.append('0');
		    }
		    sb.append(hexstr);
		}
		return sb.toString();
	}
	
	public static String randomString(int len) {
		int range = MyHelper.abc.length();
		int k = 1;
		StringBuilder sb = new StringBuilder(len);
		char c = MyHelper.abc.charAt(MyHelper.rnd.nextInt(range));
		sb.append(c);
		while(k < len) {
			int j = 0;
			c = MyHelper.abc.charAt(MyHelper.rnd.nextInt(range));
			while(j < k && c != sb.charAt(j)) {
				j++;
			}
			if(j == k) {
				sb.append(c);
				k++;
			}
		}
		return sb.toString();
	}
	
	public static Node findNodeByLabel(String label, Iterable<Node> objectlist) {
		if(label != null && label.length() > 0 && objectlist != null) {
			Iterator<Node> myIt = objectlist.iterator();
			Node obj = null;
			boolean found = false;
			while(myIt.hasNext() && !found) {
				obj = myIt.next(); 
				if(obj.getLabel().equals(label)) {
					found = true;
				}
			}
			return obj;
		}
		return null;
	}
	public static Edge findEdgeByLabel(String label, Iterable<Edge> objectlist) {
		if(label != null && label.length() > 0 && objectlist != null) {
			Iterator<Edge> myIt = objectlist.iterator();
			Edge obj = null;
			boolean found = false;
			while(myIt.hasNext() && !found) {
				obj = myIt.next(); 
				if(obj.getLabel().equals(label)) {
					found = true;
				}
			}
			return obj;
		}
		return null;
	}
	public static String getNewEdgeLabel(String pre, List<Edge> edges) {
		int edgenum = edges.size();
		boolean numberislabel = true;
		int z = 1;
		int k;
		while(numberislabel) {
			k = 0;
			boolean found;
			do {
				found = false;
				if(StringUtils.isNumeric(edges.get(k).getLabel().substring(pre.length()))) {
					if(Integer.parseInt(edges.get(k).getLabel().substring(pre.length())) != z) {
						k++;
					} else {
						found = true;
					}
				} else {
					k++;
				}
			} while (k < edgenum && !found);
			if(k < edgenum) {
				z++;
			} else {
				numberislabel = false;
			}
		}
		return pre + "" + z;  
	}
	public static String getNewNodeLabel(String pre, List<Node> nodes) {
		int nodenum = nodes.size();
		boolean numberislabel = true;
		int z = 1;
		int k;
		while(numberislabel) {
			k = 0;
			boolean found;
			do {
				found = false;
				if(StringUtils.isNumeric(nodes.get(k).getLabel().substring(pre.length()))) {
					if(Integer.parseInt(nodes.get(k).getLabel().substring(pre.length())) != z) {
						k++;
					} else {
						found = true;
					}
				} else {
					k++;
				}
			} while (k < nodenum && !found);
			if(k < nodenum) {
				z++;
			} else {
				numberislabel = false;
			}
		}
		return pre + "" + z;  
	}
	public static String getCopiedPictureUrl(Picture oldpic, Long newpicid, String folder) throws IOException {
		// file name builds up like this: folder + 'pic' + picid + '.ext'
		StringBuilder sb = new StringBuilder();
		if(oldpic.getPathOnServer() != null) {
			String ext = getExtensionFromFilename(oldpic.getPathOnServer());
			sb.append("pic");
			sb.append(newpicid.toString());
			sb.append("." + ext);
			File origfile = new File(folder + oldpic.getPathOnServer());
			File copy = new File(folder + sb.toString());
			copyFileUsingStream(origfile, copy);
		}
		return sb.toString();
	}
	
	public static String getExtensionFromFilename(String filename) {
		String temp = filename.substring(filename.length()-4).toLowerCase();
		if(temp.charAt(0) == '.')
				return temp.substring(temp.length()-3);
		else
				return temp; 
	}
	// source: https://www.journaldev.com/861/java-copy-file
	private static void copyFileUsingStream(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        if(is != null && os != null) {
	        	byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
	        }
	    } catch (Exception e) {
	    	throw new IOException();
	    } finally {
	        if(is != null && os != null) {
	        	is.close();
		        os.close();
	        }
	    }
	}
	public static byte[] getFileAsBytes(String fullpath) throws IOException {
		byte[] media;
		InputStream is = null;
		try {
			is = new FileInputStream(fullpath);
			media = is.readAllBytes();
		}
		catch (FileNotFoundException e) {
			throw new IOException(e.getMessage());
		}
		finally {
			if (is != null) {
				is.close();
			}
		}
		return media;
	}
	// source:
	// https://medium.com/@ahmedgrati1999/an-easier-way-to-upload-retrieve-images-with-spring-boot-2-0-angular-8-400d1a51dccb
	public static boolean isDirPathExistOrCreated(String dirpath) {
		boolean res = false;
		if (dirpath != null && !"".equals(dirpath)) {
			String dir;
			if(dirpath.charAt(dirpath.length()-1) == '/' || dirpath.charAt(dirpath.length()-1) == '\\') {
				dir = dirpath.substring(0, dirpath.length()-1);
			} else 
				dir = dirpath;
			try {
				Path storageDir = Paths.get(dir);
				if(!Files.exists(storageDir)) {
					Files.createDirectories(storageDir);
				}
				res = true;
			} catch (Exception e) {
				res = false;
			}
		} 
		return res;
	}
	public static <T> boolean hasSameElements(ArrayList<T> list1, ArrayList<T> list2) {
		if(list1 == null || list2 == null || list1.size() < 1 || list2.size() < 1) {
			return false;
		}
		int found = 0;
		for(T elem1 : list1) {
			for(T elem2 : list2) {
				if(elem1.equals(elem2)) {
					found++;
					break;
				}
			}
		}
		return (found == list1.size());
	}
	public static boolean isAllCloned(ArrayList<ItemWrapper> orig, ArrayList<ItemWrapper> clone) {
		boolean found = orig.size() == clone.size();
		if(found && orig.size() > 0) {
			for(ItemWrapper origitem : orig) {
				found = false;
				for(ItemWrapper cloneditem : clone) {
					if(origitem.getText().equals(cloneditem.getText()) &&
							origitem.getEdgeLabel().equals(cloneditem.getEdgeLabel())) {
						found = true;
						break;
					}
				}
				if(!found) {
					return false;
				}
			}
			return found;
		}
		return found;
	}
}
