package ivankatalenic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer {

	private List<String> lines = new ArrayList<>();
	private String fileName;

	public SVGRendererImpl(String fileName) {
		this.fileName = fileName;
		lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
	}

	public void close() throws IOException {
		lines.add("</svg>");
		File file = new File(fileName);
		Writer out = new BufferedWriter(new FileWriter(file));
		for (String line : lines) {
			out.write(line + "\n");
		}
		out.close();
	}

	@Override
	public void drawLine(Point s, Point e) {
		lines.add("<line x1=\"" + s.getX()
				+ "\" y1=\"" + s.getY()
				+ "\" x2=\"" + e.getX()
				+ "\" y2=\"" + e.getY()
				+ "\" style=\"stroke:#0000ff\"/>");
	}

	@Override
	public void fillPolygon(Point[] points) {
		StringBuilder sb = new StringBuilder("<polygon points=\"");
		for (int i = 0; i < points.length; i++) {
			Point p = points[i];
			sb.append(p.getX() + "," + p.getY() + " ");
		}
		sb.append("\" style=\"fill:#000000;\"/>");
		lines.add(sb.toString());
	}

}
