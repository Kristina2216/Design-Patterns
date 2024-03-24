package drugi;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import drugi.DeleteCommands.DeleteAfter;
import drugi.DeleteCommands.DeleteBefore;
import drugi.DeleteCommands.DeleteRange;
import drugi.fileActions.OpenAction;
import drugi.fileActions.SaveAction;
import drugi.plugins.CapitalLetterPlugin;
import drugi.plugins.StatisticsPlugin;

public class TextEditor extends JFrame implements CursorObserver, TextObserver{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TextEditorModel model;
	WhiteArea area;
	private InputMap inputMap;
	private ActionMap actionMap;
	private char[] characters = new char[] {'0','1','2','3','4','5','6','7','8','9','a','b',
			'c','d','e','f','g','h','i','j','k','l','m','n','o','p','r','s','t','u','v','z','w','q','y','x',
	        '?', '%', '~', '|',' ', ',', '-', '.', '/',';', '=', '[', '\\', ']',
	        '*', '+', ',', '-', '.', '/', '&', '*', '\"', '<', '>', '{', '}', '`', '\'',
	        '@', ':', '^', '$', '!', '(', '#', '+', ')', '_',
	        'š','ð','è','æ','ž','Š','Ð','È','Æ','Ž',10};
	private ClipboardStack stack=new ClipboardStack();
	
	public TextEditor(TextEditorModel m, WhiteArea a) {
		model=m;
		model.addCursorObserver(this);
		model.addTextObserver(this);
		area=a;
		getContentPane().add(a);
		area.setVisible(true);
		inputMap=area.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap=area.getActionMap();
		createActions();
		createMenu();
		initGUI();
	}
	
	public TextEditorModel getModel() {
		return model;
	}
	
	private void initGUI() {
        setSize(500, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("TextEditor");
    }
	
	
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TextEditorModel model = new TextEditorModel("Primjer teksta\nNovi redak\nJos jedan novi.");
            WhiteArea platno=new WhiteArea(model);
            TextEditor frame = new TextEditor(model, platno);
            frame.setVisible(true);
        });
    }
	
	private CursorCommand cursorDown = new CursorCommand("Down", "cursorDown") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!model.hasCursor())
				model.initCursor();
			model.moveCursorDown();
		}
	};
	
	private CursorCommand cursorUp = new CursorCommand("Up", "cursorUp") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!model.hasCursor())
				model.initCursor();
			model.moveCursorUp();
			
		}
	};
	
	private CursorCommand cursorLeft = new CursorCommand("Left", "cursorLeft") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!model.hasCursor())
				model.initCursor();
			model.moveCursorLeft();
		}
	};
	private CursorCommand cursorRight = new CursorCommand("Right", "cursorRight") {
		
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!model.hasCursor())
				model.initCursor();
			model.moveCursorRight();
		}
	};
	
	private Action exit = new AbstractAction("Exit") {
			
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e) {
					System.exit(0);
			}
		};
		
	
	
	private void createActions() { 
		inputMap.put(KeyStroke.getKeyStroke("DOWN"), "Down");
		actionMap.put("Down",
                cursorDown);
		inputMap.put(KeyStroke.getKeyStroke("UP"), "Up");
		actionMap.put("Up",
                cursorUp);
		inputMap.put(KeyStroke.getKeyStroke("LEFT"), "Left");
		actionMap.put("Left",
                cursorLeft);
		inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "Right");
		actionMap.put("Right",
                cursorRight);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "BackSpace");
		actionMap.put("BackSpace", new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
	            public void actionPerformed(ActionEvent e) {
				if(model.isSelected()) {
					DeleteRange dr=new DeleteRange(model);
					dr.makeChanges();
					return;
				}
				 DeleteBefore bf=new DeleteBefore(model);
				 bf.makeChanges();
			 }
		});
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, KeyEvent.SHIFT_DOWN_MASK), "SelectLeft");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, KeyEvent.SHIFT_DOWN_MASK), "SelectRight");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, KeyEvent.SHIFT_DOWN_MASK), "SelectUp");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, KeyEvent.SHIFT_DOWN_MASK), "SelectDown");
		
		actionMap.put("SelectLeft", new CursorWithShift(cursorLeft));
		actionMap.put("SelectRight", new CursorWithShift(cursorRight));
		actionMap.put("SelectUp", new CursorWithShift(cursorUp));
		actionMap.put("SelectDown", new CursorWithShift(cursorDown));
		
		inputMap.put(KeyStroke.getKeyStroke("DELETE"), "Delete");
		actionMap.put("Delete", new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
	            public void actionPerformed(ActionEvent e) {
				if(model.isSelected()) {
					DeleteRange dr=new DeleteRange(model);
					dr.makeChanges();
					return;
				}
				 DeleteAfter bf=new DeleteAfter(model);
				 bf.makeChanges();
			 }
		});
		
		for(char c: characters) {
			inputMap.put(KeyStroke.getKeyStroke(c), Character.toString(c));
			actionMap.put(Character.toString(c), new AbstractAction() {
				
				private static final long serialVersionUID = 1L;

				@Override
		            public void actionPerformed(ActionEvent e) {
					 model.insert(c, false);
				 }
			});
		}
		
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "CTRL_C");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), "CTRL_X");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "CTRL_V");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), "CTRL_SHIFT_V");
		
		actionMap.put("CTRL_C",new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
	        public void actionPerformed(ActionEvent e) {
				stack.push(model.getSelectedText());
			 }
		}); 
		
		actionMap.put("CTRL_X",new AbstractAction() {
					
					private static final long serialVersionUID = 1L;
		
					@Override
			        public void actionPerformed(ActionEvent e) {
						stack.push(model.getSelectedText());
						model.setSelectedRange(null);
					 }
				}); 
		
		actionMap.put("CTRL_SHIFT_V",new AbstractAction() {
			
			private static final long serialVersionUID = 1L;

			@Override
	        public void actionPerformed(ActionEvent e) {
				String add=stack.pop();
				model.insert(add);
			 }
		}); 
		
	actionMap.put("CTRL_V",new AbstractAction() {
				
				private static final long serialVersionUID = 1L;
	
				@Override
		        public void actionPerformed(ActionEvent e) {
					String add=stack.peek();
					model.insert(add);
				 }
			}); 
		 
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "CTRL_Z");
	inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "CTRL_Y");
	actionMap.put("CTRL_Z", new AbstractAction() {
				
				private static final long serialVersionUID = 1L;
	
				@Override
		        public void actionPerformed(ActionEvent e) {
					UndoManager.getInstance().undo();
				 }
			}); 
	
	actionMap.put("CTRL_Y", new AbstractAction() {
		
		private static final long serialVersionUID = 1L;

		@Override
        public void actionPerformed(ActionEvent e) {
			UndoManager.getInstance().repeat();
		 }
	}); 
 
}
	
	public void createMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu();
		fileMenu.setText("File");
		menuBar.add(fileMenu);

		fileMenu.add(new JMenuItem(new OpenAction(model)));
		fileMenu.add(new JMenuItem(new SaveAction(model)));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(exit));
	
		
		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);
		
		JButton undo= new JButton(actionMap.get("CTRL_Z"));
		undo.setText("undo");
		toolBar.add(undo);
		JButton redo= new JButton(actionMap.get("CTRL_Y"));
		redo.setText("redo");
		toolBar.add(undo);
		JButton copy= new JButton(actionMap.get("CTRL_C"));
		copy.setText("copy");
		toolBar.add(copy);
		JButton paste= new JButton(actionMap.get("CTRL_V"));
		paste.setText("paste");
		toolBar.add(paste);
		JButton del=new JButton(actionMap.get("Delete"));
		del.setText("del");
		
		JMenu cursorMenu = new JMenu();
		cursorMenu.setText("Move");
		menuBar.add(cursorMenu);
		JMenuItem start=new JMenuItem(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCursorLocation(new Location(0,0));
				
			}
			
		});
		start.setText("toStart");
		JMenuItem end=new JMenuItem(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.setCursorLocation(new Location(model.range.getMax().getX(),model.range.getMax().getY()));
				
			}
			
		});
		end.setText("toEnd");
		cursorMenu.add(start);
		cursorMenu.add(end);
		
		JMenu pluginMenu = new JMenu();
		pluginMenu.setText("Plugins");
		menuBar.add(pluginMenu);
		JMenuItem stats=new JMenuItem(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				StatisticsPlugin s=new StatisticsPlugin();
				s.execute(model, UndoManager.getInstance(), stack);
				
			}
			
		});
		stats.setText("Statistics");
		pluginMenu.add(stats);
		JMenuItem capitalize=new JMenuItem(new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				CapitalLetterPlugin s=new CapitalLetterPlugin();
				s.execute(model, UndoManager.getInstance(), stack);
				
			}
			
		});
		capitalize.setText("Capitalize");
		pluginMenu.add(capitalize);
		

		this.getContentPane().add(toolBar, BorderLayout.PAGE_START);
		this.setJMenuBar(menuBar);
		this.getContentPane().add(new StatusBar(model), BorderLayout.SOUTH);
	}
	
	
	private class CursorWithShift extends AbstractAction{
		CursorCommand func;
		
		public CursorWithShift(CursorCommand func) {
			this.func=func;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!model.isSelected())
				model.setSelectedRangeBegin(model.getCursorLocation());
			func.actionPerformed(e);
			model.setSelectedRangeEnd(model.getCursorLocation());
			
		}
		
	}

	@Override
	public void updateCursorLocation(Location loc) {
		repaint();
		
	}

	@Override
	public void updateText() {
		repaint();
		
	}
	
	

}


