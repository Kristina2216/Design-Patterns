package prvi;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import prvigraphicalObjects.CompositeShape;
import prvigraphicalObjects.GraphicalObject;
import prvigraphicalObjects.LineSegment;
import prvigraphicalObjects.Oval;
import prvigraphicalObjects.Point;
import states.AddShapeState;
import states.EraserState;
import states.IdleState;
import states.SelectShapeState;
import states.State;

public class GUI extends JFrame{
	/**
	 * 
	 */
	private Canvas canvas;
	private DocumentModel model;
	private State currentState;
	List<GraphicalObject> objects=new ArrayList<GraphicalObject>();
	private InputMap inputMap;
	private ActionMap actionMap;
	
	
	public class Canvas extends JComponent implements DocumentModelListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private GUI gui;
		
		public Canvas(GUI g) {
            super();
            setVisible(true);
            setFocusable(true);
            
            this.gui=g;
            gui.model.addDocumentModelListener(() -> {
            	Canvas.this.repaint();
            	});
        }

		@Override
        protected void paintComponent(Graphics g) {
			requestFocusInWindow();
            super.paintComponent(g);
            Renderer r = new G2DRendererImpl((Graphics2D)g);
            for(GraphicalObject o : gui.model.list()) {
                o.render(r);
                currentState.afterDraw(r, o);
            }
            currentState.afterDraw(r);
        }

		@Override
		public void documentChanged() {
			repaint();
			
		} 


	}
	
	
	public GUI(List<GraphicalObject> objects) {
		model = new DocumentModel();
		for(GraphicalObject o: objects) {
			this.objects.add(o);
			model.addGraphicalObject(o);
		}
		canvas=new Canvas(this);
		currentState=new IdleState();
		inputMap=canvas.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		actionMap=canvas.getActionMap();
		makeActions();
		setMouseListeners();
		setKeyboardListeners();
		initGUI();
	}
	
	private void initGUI() {
		setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        add(canvas, BorderLayout.CENTER);

        setTitle("Lab4");
        makeButtons();
        
	}
	
	private void makeButtons() {

		JToolBar toolBar = new JToolBar("Alati");
		toolBar.setFloatable(true);
		for(GraphicalObject go: objects) {
			JButton b= new JButton(new AbstractAction() {
				
				private static final long serialVersionUID = 1L;
		
				@Override
		            public void actionPerformed(ActionEvent e) {
					currentState.onLeaving();
		            currentState = new AddShapeState(model, go);
					}
				});
			b.setText(go.getShapeName());
			toolBar.add(b);
		}
		JButton selection= new JButton(new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
	            public void actionPerformed(ActionEvent e) {
				currentState.onLeaving();
	            currentState = new SelectShapeState(model);
				}
			});
		selection.setText("Selektiraj");
		toolBar.add(selection);
		
		JButton delete= new JButton(new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
	            public void actionPerformed(ActionEvent e) {
				currentState.onLeaving();
	            currentState = new EraserState(model, canvas);
				}
			});
		delete.setText("Brisalo");
		toolBar.add(delete);

		JButton svg= new JButton(new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
	            public void actionPerformed(ActionEvent e) {
				openSVGDialog();
				}
			});
		svg.setText("SVG Export");
		toolBar.add(svg);
		this.add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
		
		JButton save= new JButton(new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
	            public void actionPerformed(ActionEvent e) {
				openSaveDialog();
				}
			});
		save.setText("Pohrani");
		toolBar.add(save);
		this.add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
		
		JButton load= new JButton(new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
	            public void actionPerformed(ActionEvent e) {
				openLoadDialog();
				}
			});
		load.setText("Uèitaj");
		toolBar.add(load);
		this.add(toolBar, BorderLayout.BEFORE_FIRST_LINE);
	}
	
	public void makeActions() {
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Esc");
		actionMap.put("Esc", new AbstractAction() {
			
			private static final long serialVersionUID = 1L;
	
			@Override
	            public void actionPerformed(ActionEvent e) {
					currentState.onLeaving();
					currentState=new IdleState();
				}
			});
		
	}
	
	private void setMouseListeners(){
		canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = new Point(e.getX(), e.getY());
                currentState.mouseDown(point, e.isShiftDown(), e.isControlDown());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point point = new Point(e.getX(), e.getY());
                currentState.mouseUp(point, e.isShiftDown(), e.isControlDown());
            }
        });

        canvas.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point = new Point(e.getX(), e.getY());
                currentState.mouseDragged(point);
            }
        });
	}
	
	private void setKeyboardListeners() {
		
		 canvas.addKeyListener(new KeyAdapter() {
			 @Override
			 public void keyPressed(KeyEvent e) {
	                 currentState.keyPressed(e.getKeyCode());
			 }
			});

	}
	
	public static void main(String[] args) {
		  SwingUtilities.invokeLater(() -> {
	            List<GraphicalObject> objects = new ArrayList<>();
	            objects.add(new LineSegment(new Point(10,20), new Point(200,220)));
	            objects.add(new Oval(new Point(50, 100),new Point(100,50)));
	            GUI gui = new GUI(objects);
	            gui.setVisible(true);
	        });

	}
	
	private void openSVGDialog() {
		JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String name = fileChooser.getSelectedFile().getAbsolutePath();
            SVGRendererImpl r = new SVGRendererImpl(name);
            for (GraphicalObject object : model.list()) {
                object.render(r);
            }

            try {
                r.close();
            } catch (IOException ex) {
                
            }
        }

	}
	
	private void openSaveDialog() {
		JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        	String p = fileChooser.getSelectedFile().getAbsolutePath();
        	Path path=Paths.get(p);
            List<String> lines = new ArrayList<>();
            for (GraphicalObject object : model.list()) {
                object.save(lines);
            }try {
                Files.write(path, lines);
            }catch (IOException ex) {
            }
        }
	}
	
	private void openLoadDialog() {
		 JFileChooser fileChooser = new JFileChooser();
	        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	            Path path = fileChooser.getSelectedFile().toPath();
	            try {
	                List<String> lines = Files.readAllLines(path);
	                List<String> id=new ArrayList<String>();
	                List<GraphicalObject> obj=new ArrayList<GraphicalObject>();
	                for(GraphicalObject o:objects) {
	                	obj.add(o);
	                	id.add(o.getShapeID());
	                }
	                CompositeShape composite = new CompositeShape(null);
	                obj.add(composite);
	                id.add(composite.getShapeID());
	                Stack<GraphicalObject> stack = new Stack<>();
	                for (String line : lines) {
	                    if (!line.strip().isEmpty()) {
		                    String[] data = line.split("\\s+", 2);
		                    int index=id.indexOf(data[0]);
		                    if(index!=-1)
		                    	obj.get(index).load(stack, data[1]);
	                    }
	                }

	                model.clear();
	                stack.forEach(model::addGraphicalObject);
	            }catch (IOException|RuntimeException e) {
	            
	            }
	        }
	}
}
