package gws.grottworkshop.gwstableapl.android;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import gws.grottworkshop.gwstableapl.android.AndroidToolkit.DebugRect;
import gws.grottworkshop.gwstableapl.layout.BaseTableLayout;
import gws.grottworkshop.gwstableapl.layout.BaseTableLayout.Debug;
import gws.grottworkshop.gwstableapl.layout.Cell;

@SuppressWarnings("unused")
class TableLayout extends BaseTableLayout<View, Table, TableLayout, AndroidToolkit> {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	ArrayList<View> otherChildren = new ArrayList(1);
	ArrayList<DebugRect> debugRects;

	public TableLayout () {
		super((AndroidToolkit)AndroidToolkit.instance);
	}

	public TableLayout (AndroidToolkit toolkit) {
		super(toolkit);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Cell add (View widget) {
		Cell cell = super.add(widget);
		otherChildren.remove(widget);
		return cell;
	}

	@SuppressWarnings("rawtypes")
	public void layout (float layoutX, float layoutY, float layoutWidth, float layoutHeight) {
		List<Cell> cells = getCells();
		for (int i = 0, n = cells.size(); i < n; i++) {
			Cell c = cells.get(i);
			if (c.getIgnore()) continue;
			((View)c.getWidget()).layout((int)c.getWidgetX(), (int)c.getWidgetY(), //
				(int)(c.getWidgetX() + c.getWidgetWidth()), //
				(int)(c.getWidgetY() + c.getWidgetHeight()));
		}

		super.layout(layoutX, layoutY, layoutWidth, layoutHeight);

		for (int i = 0, n = cells.size(); i < n; i++) {
			Cell c = cells.get(i);
			if (c.getIgnore()) continue;
			((View)c.getWidget()).layout((int)c.getWidgetX(), (int)c.getWidgetY(), //
				(int)(c.getWidgetX() + c.getWidgetWidth()), //
				(int)(c.getWidgetY() + c.getWidgetHeight()));
		}

		for (int i = 0, n = otherChildren.size(); i < n; i++) {
			View child = otherChildren.get(i);
			child.layout(0, 0, (int)layoutWidth, (int)layoutHeight);
		}
	}

	void invalidateSuper () {
		super.invalidate();
	}

	public void invalidate () {
		super.invalidate();
		getTable().requestLayout();
	}

	public void invalidateHierarchy () {
		super.invalidate();
		getTable().requestLayout();
	}

	public void drawDebug (Canvas canvas) {
		if (getDebug() == Debug.none || debugRects == null) return;
		Paint paint = AndroidToolkit.getDebugPaint();
		for (int i = 0, n = debugRects.size(); i < n; i++) {
			DebugRect rect = debugRects.get(i);
			int r = rect.type == Debug.cell ? 255 : 0;
			int g = rect.type == Debug.widget ? 255 : 0;
			int b = rect.type == Debug.table ? 255 : 0;
			paint.setColor(Color.argb(255, r, g, b));
			canvas.drawRect(rect.rect, paint);
		}
	}
}
