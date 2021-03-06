package software_masters.gui_test;

import static org.testfx.api.FxAssert.verifyThat;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import businessPlannerApp.Main;
import businessPlannerApp.backend.ServerImplementation;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

/**
 * @author software masters This class contains methods that are common to all
 *         of the gui tests. It is the testing super class.
 */
public abstract class GuiTestBase extends ApplicationTest {
	Stage stage;
	/**
	 * This closes the window and clears any action event after a test is executed.
	 *
	 * @throws TimeoutException
	 */
	@AfterAll
	public static void afterAllTest() throws TimeoutException {
		FxToolkit.hideStage();
		FxToolkit.cleanupStages();
	}

	/**
	 * This resets keyboard and mouse events after each sub test.
	 */
	@SuppressWarnings("deprecation")
	@AfterEach
	public void afterEachTest() {
//		try {
//			FxToolkit.hideStage();
//		} catch (TimeoutException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Platform.runLater(stage::close);
		this.release(new KeyCode[] {});
		this.release(new MouseButton[] {});
	}

	/**
	 * Helper method for checking popups for the right error message
	 *
	 * @param msg
	 */
	public void checkPopupMsg(String msg) { verifyThat(msg, LabeledMatchers.hasText(msg)); }

	/**
	 * Helper method for grabbing nodes.
	 *
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Node> T find(final String query) { return (T) this.lookup(query).queryAll().iterator().next(); }

	/**
	 * Spawns the gui.
	 */
	@BeforeEach
	public void setUpBeforeEachTest() {
		try {
			ServerImplementation.testSpawn();
			ApplicationTest.launch(Main.class);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception { this.stage=stage; stage.show(); }

	/**
	 * Helper method that simplifies duplicate code for checking nodes with labels.
	 *
	 * @param id
	 * @param text
	 */
	public void verify(String id, String text) {
		verifyThat(id, (val) -> { return ((Labeled) val).getText().equals(text); });
	}

	/**
	 * Helper method that simplifies duplicate code for checking nodes with labels.
	 *
	 * @param id
	 * @param text
	 */
	public void verifyField(String id, String text) {
		verifyThat(id, (val) -> { return ((TextField) val).getText().equals(text); });
	}

}
