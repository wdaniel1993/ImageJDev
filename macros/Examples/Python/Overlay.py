# This script demonstrates how to use an Overlay to display
# graphics and text non-destructively on an image.

imp = IJ.openImage("http://imagej.nih.gov/ij/images/blobs.gif")
overlay = Overlay()
font = Font("SanSerif", Font.PLAIN, 28)
roi = TextRoi(10, 5, "This is an overlay", font)
roi.setStrokeColor(Color.yellow)
roi.setFillColor(Color(0,0,0,0.5))
overlay.add(roi)
roi = Roi(30,70,200,150)
roi.setStrokeColor(Color.blue)
roi.setFillColor(Color(0,0,1,0.3))
overlay.add(roi)
roi = OvalRoi(60,60,140,140)
roi.setStrokeColor(Color.green)
roi.setStrokeWidth(15)
overlay.add(roi)
roi = Line(30,70,230,230)
roi.setStrokeColor(Color.red)
roi.setStrokeWidth(18)
overlay.add(roi)
x = [18,131,148,242]
y = [167,104,232,172]
roi = PolygonRoi(x, y, len(x), Roi.POLYLINE)
roi.fitSpline()
roi.setStrokeColor(Color.blue)
roi.setStrokeWidth(12)
imp.setRoi(roi)
overlay.add(roi)
imp.setOverlay(overlay)
imp.show()
