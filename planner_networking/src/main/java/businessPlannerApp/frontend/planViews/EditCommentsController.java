package businessPlannerApp.frontend.planViews;

import java.rmi.RemoteException;
import java.util.Optional;

import businessPlannerApp.Main;
import businessPlannerApp.backend.Comment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

public class EditCommentsController extends EditController implements CommentController {

	@FXML
	ListView<Comment> commentList;

	@Override
	public void changeSection() {
		super.changeSection();
		this.setListView();
		this.commentList.refresh();
	}

	@Override
	public void createComment() {
		final TextInputDialog comment = new TextInputDialog();
		comment.setTitle("New Comment");
		final Optional<String> result = comment.showAndWait();
		if (result.isPresent()) try {
			this.model.getCurrNode().addComment(new Comment(result.get(), this.model.getUsername()));
			this.setListView();
			this.commentList.refresh();
			this.isPushed = false;
		} catch (final IllegalArgumentException e) {
			this.logout();
		} catch (final RemoteException e) {
			this.app.showConnectToServer();
		}
	}

	@Override
	public void hideComments() {
		this.changeSection();
		this.app.showPlanView(this.selfPath);
	}

	/**
	 * Let controller to know view
	 *
	 * @param application
	 */
	@Override
	public void setApplication(Main application) {
		super.setApplication(application);
		this.setListView();
	}

	private void setListView() {
		ObservableList<Comment> items = null;
		items = FXCollections.observableList(this.model.getCurrNode().getUnresolvedComments());
		this.commentList.setItems(items);
	}

	@Override
	public void viewComment() {
		final Comment comment = this.commentList.getSelectionModel().getSelectedItem();
		if (comment == null) return;

		final Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText(comment.getContent());
		final ButtonType resolvedButton = new ButtonType("Resolved");
		final ButtonType cancelButton = new ButtonType("Cancel");
		alert.getButtonTypes().setAll(resolvedButton, cancelButton);
		final Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == resolvedButton) {
			comment.setResolved(true);
			this.setListView();
			this.commentList.refresh();
			this.isPushed = false;
		}
	}

}
