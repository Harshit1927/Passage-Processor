import java.util.ArrayList;

public class Tree {

	public TreeNode root;

	public Tree() {
		root = null;
	}

	public void resolve(TreeNode x, TreeNode par){
		if (x!=null && x.s.size()>=4){
			TreeNode nx = new TreeNode();
			for (int i=2; i<x.s.size(); i++){
				nx.s.add(x.s.get(i));
				nx.val.add(x.val.get(i));
			}
			for (int i=2; i<x.children.size(); i++){
				nx.children.add(x.children.get(i));
			}
			nx.height = x.height;
			while (x.children.size()>2){
				x.children.remove(x.children.size()-1);
			}
			while (x.s.size()>2){
				x.s.remove(x.s.size()-1);
				x.val.remove(x.val.size()-1);
			}
			if (par==null){
				root = new TreeNode();
				root.height = x.height+1;
				root.s.add(x.s.get(x.s.size()-1));
				root.val.add(x.val.get(x.val.size()-1));
				root.children.add(x);
				root.children.add(nx);
				x.val.remove(x.val.size()-1);
				x.s.remove(x.s.size()-1);
			} else {
				int don = 0;
				for (int i=0; i<par.s.size(); i++){
					if (par.s.get(i).compareTo(x.s.get(x.s.size()-1))>0){
						par.s.add(i, x.s.get(x.s.size()-1));
						par.val.add(i, x.val.get(x.val.size()-1));						
						don++;
						break;
					}
				}
				if (don==0){
					par.s.add(x.s.get(x.s.size()-1));
					par.val.add(x.val.get(x.val.size()-1));
				}
				x.s.remove(x.s.size()-1);
				x.val.remove(x.val.size()-1);
				for (int i = 0; i<par.children.size(); i++){
					if (par.children.get(i)==x){
						par.children.add(i+1, nx);
						break;
					}
				}
			}
		}
	}

	public void ins(String s, TreeNode cur, TreeNode par){
		if (cur == null){
			root = new TreeNode();
			root.s.add(s);
			root.val.add(1);
		} else {
			if (cur.children.size()==0){
				int don = 0;
				for (int i=0; i<cur.s.size(); i++){
					if (cur.s.get(i).compareTo(s)>0){
						cur.s.add(i, s);
						cur.val.add(i, 1);
						don++;
						break;
					}
				}
				if (don==0){
					cur.s.add(s);
					cur.val.add(1);
				}
			} else {
				int don = 0;
				for (int i=0; i<cur.s.size(); i++){
					if (cur.s.get(i).compareTo(s)>0){
						don++;
						ins(s, cur.children.get(i), cur);
						break;
					}
				}
				if (don==0){
					ins(s, cur.children.get(cur.children.size()-1), cur);
				}
			}
			// preorder();
			resolve(cur, par);
		}
	}

	public void insert(String s) {
		// TO be completed by students
		// System.out.println(s);
		ins(s, root, null);
		// resolve(root, null);
	}

	public void resolution(TreeNode cur, TreeNode par){
		if (cur.s.size()>0){
			return;
		} else {
			if (par==null){
				if (cur.children.size()==0){
					root = null;
				} else {
					root = cur.children.get(0);
				}
			} else {
				if (par.children.get(0)==cur){
					if (par.children.get(1).s.size()>1){
						cur.s.add(par.s.get(0));
						cur.val.add(par.val.get(0));
						par.s.set(0, par.children.get(1).s.get(0));
						par.val.set(0, par.children.get(1).val.get(0));
						par.children.get(1).s.remove(0);
						par.children.get(1).val.remove(0);
					} else {
						TreeNode rt = par.children.get(1);
						rt.s.add(0, par.s.get(0));
						rt.val.add(0, par.val.get(0));
						par.s.remove(0);
						par.val.remove(0);
						for (int i=0; i<cur.children.size(); i++){
							rt.children.add(i, cur.children.get(i));
						}
						par.children.remove(0);
					}
				} else {
					int ind = 0;
					for (; ind<par.children.size(); ind++){
						if (par.children.get(ind)==cur){
							break;
						}
					}
					if (par.children.get(ind-1).s.size()>1){
						cur.s.add(par.s.get(ind-1));
						cur.val.add(par.val.get(ind-1));
						int sz = par.children.get(ind-1).s.size();
						par.s.set(0, par.children.get(ind-1).s.get(sz-1));
						par.val.set(0, par.children.get(ind-1).val.get(sz-1));
						par.children.get(ind-1).s.remove(sz-1);
						par.children.get(ind-1).val.remove(sz-1);
					} else {
						TreeNode rt = par.children.get(ind-1);
						rt.s.add(par.s.get(ind-1));
						rt.val.add(par.val.get(ind-1));
						par.s.remove(ind-1);
						par.val.remove(ind-1);
						for (int i=0; i<cur.children.size(); i++){
							rt.children.add(cur.children.get(i));
						}
						par.children.remove(ind);
					}
				}
			}
		}
	}

	public void del2(String s, TreeNode was_cur, TreeNode cur, TreeNode par, int wh){
		if (cur.children.size()==0){
			was_cur.s.set(wh, cur.s.get(cur.s.size()-1));
			was_cur.val.set(wh, cur.val.get(cur.s.size()-1));
			cur.s.remove(cur.s.size()-1);
			cur.val.remove(cur.val.size()-1);
		} else {
			del2(s, was_cur, cur.children.get(cur.children.size()-1), cur, wh);
		}
		resolution(cur, par);
	}

	public void del(String s, TreeNode cur, TreeNode par){
		if (cur==null){
			return;
		} else {
			int don = 0;
			for (int i=0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s);
				// System.out.println(cur.s.get(i)+"  pl"+comp+"  kol   "+s);
				if (comp==0){
					if (cur.children.size()==0){
						cur.s.remove(i);
						cur.val.remove(i);
						// System.out.println("  here");
					} else {
						del2(s, cur, cur.children.get(i), cur, i);
					}
					don++;
					break;
				} else if (comp>0){
					del(s, cur.children.get(i), cur);
					don++;
					break;
				}
			}
			if (don==0){
				// System.out.println(cur.children.size()+"  cur "+cur.s.get(0));
				del(s, cur.children.get(cur.children.size()-1), cur);
			}
			resolution(cur, par);  // balancing the 2-4 tree
		}
	}

	public boolean delete(String s) {
		// System.out.println("del  "+s);
		// TO be completed by students
		if (search(s)==false){
			return false;
		} else {
			// System.out.println(root.s.get(0)+"  vl");
			del(s, root, null);
			return true;
		}
	}

	public boolean search(String s) {
		// System.out.println("searc "+s);
		// TO be completed by students
		TreeNode cur = root;
		// for (int i = 0; i<cur.s.size()-1; i++){
		// 	System.out.print(cur.s.get(i)+"  k  ");
		// }
		while (cur!=null){
			int don = 0;
			// for (int i = 0; i<cur.s.size(); i++){
			// 	System.out.print(cur.s.get(i)+" ");
			// }
			// System.out.println();
			for (int i = 0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s);
				if (comp>0){
					if (cur.children.size()==0){
						return false;
					} else {
						cur = cur.children.get(i);
					}
					don++;
					break;
				} else if (comp==0){
					return true;
				}
			}
			if (don==0){
				if (cur.children.size()==0){
					return false;
				} else {
					cur = cur.children.get(cur.children.size()-1);
				}
			}
		}
		return false;
	}
	
	public int increment(String s) {
		// System.out.println("inc "+s);
		// TO be completed by students
		TreeNode cur = root;
		// for (int i = 0; i<cur.s.size()-1; i++){
		// 	System.out.print(cur.s.get(i)+"  k  ");
		// }
		while (cur!=null){
			int don = 0;
			// for (int i = 0; i<cur.s.size(); i++){
			// 	System.out.print(cur.s.get(i)+" ");
			// }
			// System.out.println();
			for (int i = 0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s);
				if (comp>0){
					if (cur.children.size()==0){
						assert false:"Increment called on non-existent String";
					} else {
						cur = cur.children.get(i);
					}
					don++;
					break;
				} else if (comp==0){
					int jk = cur.val.get(i);
					cur.val.set(i, jk+1);
					return jk+1;
				}
			}
			if (don==0){
				if (cur.children.size()==0){
					assert false:"Increment called on non-existent String";
				} else {
					cur = cur.children.get(cur.children.size()-1);
				}
			}
		}
		assert false:"Increment called on non-existent String";
		return -1;
	}

	public int decrement(String s) {
		// System.out.println("dec "+s);
		// TO be completed by students
		TreeNode cur = root;
		// for (int i = 0; i<cur.s.size()-1; i++){
		// 	System.out.print(cur.s.get(i)+"  k  ");
		// }
		while (cur!=null){
			int don = 0;
			// for (int i = 0; i<cur.s.size(); i++){
			// 	System.out.print(cur.s.get(i)+" ");
			// }
			// System.out.println();
			for (int i = 0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s);
				if (comp>0){
					if (cur.children.size()==0){
						// System.out.println(" aser");
						// assert false:"Decrement called on non-existent String";
						break;
					} else {
						cur = cur.children.get(i);
					}
					don++;
					break;
				} else if (comp==0){
					int jk = cur.val.get(i);
					cur.val.set(i, jk-1);
					return jk-1;
				}
			}
			if (don==0){
				if (cur.children.size()==0){
					// System.out.println(" aser");
					assert false:"Decrement called on non-existent String";
					break;
				} else {
					cur = cur.children.get(cur.children.size()-1);
				}
			}
		}
		// System.out.println(" aser");
		assert false:"Decrement called on non-existent String";
		return -1;
	}

	public int getHeight() {
		// TO be completed by students
		// System.out.println("gh "+ "pl");
		if (root == null){
			return 0;
		}
		return root.height;
	}

	public int getVal(String s) {
		// TO be completed by students
		// System.out.println("gv "+s);
		TreeNode cur = root;
		// for (int i = 0; i<cur.s.size()-1; i++){
		// 	System.out.print(cur.s.get(i)+"  k  ");
		// }
		while (cur!=null){
			int don = 0;
			// for (int i = 0; i<cur.s.size(); i++){
			// 	System.out.print(cur.s.get(i)+" ");
			// }
			// System.out.println();
			for (int i = 0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s);
				if (comp>0){
					if (cur.children.size()==0){
						assert false:"GetVal called on non-existent String";
					} else {
						cur = cur.children.get(i);
					}
					don++;
					break;
				} else if (comp==0){
					int jk = cur.val.get(i);
					return jk;
				}
			}
			if (don==0){
				if (cur.children.size()==0){
					assert false:"GetVal called on non-existent String";
				} else {
					cur = cur.children.get(cur.children.size()-1);
				}
			}
		}
		assert false:"GetVal called on non-existent String";
		return -1;
	}

}
