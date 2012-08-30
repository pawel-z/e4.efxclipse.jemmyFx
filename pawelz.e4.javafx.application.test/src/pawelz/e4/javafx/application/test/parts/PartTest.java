package pawelz.e4.javafx.application.test.parts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;

import org.jemmy.fx.NodeDock;
import org.jemmy.fx.SceneDock;
import org.jemmy.fx.control.ListItemDock;
import org.jemmy.fx.control.ListViewDock;
import org.jemmy.lookup.LookupCriteria;
import org.junit.BeforeClass;
import org.junit.Test;

import pawelz.e4.javafx.application.model.Media;
import pawelz.e4.javafx.application.model.MediaType;

import com.sun.javafx.scene.control.skin.LabeledText;

/**
 * 
 * Simple test class
 * 
 */
@SuppressWarnings("restriction")
public class PartTest {

	protected static SceneDock scene;

	@BeforeClass
	public static void startApp() throws InterruptedException {
		scene = new SceneDock();
	}

	@Test
	public void labelTest() {
		NodeDock label = new NodeDock(getBox().asParent(), Label.class, "label_1");

		NodeDock nodeDock = new NodeDock(label.asParent(), LabeledText.class);
		String text = ((LabeledText) nodeDock.wrap().getControl()).getText();

		assertTrue(text.equals("Simple label") || text.equals("button has been clicked"));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void listViewTest() {
		ListViewDock listViewDock = new ListViewDock(getBox().asParent());
		// get item from list

		LookupCriteria<Object> lookupCriteria = new LookupCriteria<Object>() {

			@Override
			public boolean check(Object arg0) {
				return arg0 instanceof Media && ((Media) arg0).getType() == MediaType.PICTURE;
			}
		};

		ListItemDock item = new ListItemDock(listViewDock.asList(), lookupCriteria);
		// FIXME: it does not work as well.
		// item.asEditableCell().select();
	}

	@Test
	public void buttonTest() {
		NodeDock button = new NodeDock(getBox().asParent(), Button.class, "button_1");
		clickOnButton(button);

		NodeDock label = new NodeDock(getBox().asParent(), Label.class, "label_1");
		NodeDock nodeDock = new NodeDock(label.asParent(), LabeledText.class);
		String text = ((LabeledText) nodeDock.wrap().getControl()).getText();

		assertEquals("button has been clicked", text);
	}

	@Test
	public void moveMouseTest() {
		NodeDock button = new NodeDock(getBox().asParent(), Button.class, "button_1");
		// FIXME: it does not work
		// button.wrap().mouse().move();
	}

	protected NodeDock getBox() {
		return new NodeDock(scene.asParent(), VBox.class, "vbox_1");
	}

	protected void clickOnButton(NodeDock button) {
		/*
		 * I should be able to click on the button using this line:
		 * button.wrap().mouse().click();
		 * 
		 * But it does not work in e4-javafx application
		 */
		Node control = button.wrap().getControl();
		if (control instanceof ButtonBase) {
			clickOnFxButton((ButtonBase) control);
			return;
		}

		// FIXME: here is a problem
		// Mouse mouse = button.wrap().mouse();
		// mouse.click();
	}

	public static void clickOnFxButton(final ButtonBase button) {

		/*
		 * because test cases are running out of the FX application thread we
		 * have to click on this button inside a safe runner
		 */
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (button instanceof ToggleButton) {
					((ToggleButton) button).setSelected(true);
				} else {
					button.fire();
				}
			}
		});

	}

}
