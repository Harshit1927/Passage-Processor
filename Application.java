import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Application extends Tree {

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
			upd(x);
			upd(nx);
			upd(par);
		}
	}

	public void upd(TreeNode cur){
		if (cur==null){
			return;
		} else {
			cur.max_value = -1;
			cur.count = 0;
			for (TreeNode x:cur.children){
				if (x.max_value>cur.max_value){
					cur.max_value = x.max_value;
					cur.max_s = x.max_s;
				} else if (x.max_value == cur.max_value){
					if (cur.max_s.compareTo(x.max_s)>0){
						cur.max_s = x.max_s;
					}
				}
				cur.count+=x.count;
			}
			for (int i=0; i<cur.val.size(); i++){
				int cval = cur.val.get(i);
				if (cval>cur.max_value){
					cur.max_s = cur.s.get(i);
					cur.max_value = cval;
				} else if (cval==cur.max_value){
					String jk = cur.s.get(i);
					if (jk.compareTo(cur.max_s)<0){
						cur.max_s = jk;
					}
				}
				cur.count+=cval;
			}
		}
	}

	public void ins(String s, TreeNode cur, TreeNode par){
		if (cur == null){
			root = new TreeNode();
			root.s.add(s);
			root.val.add(1);
			root.count++;
		} else {
			cur.count++;
			if (cur.children.size()==0){
				int don = 0;
				for (int i=0; i<cur.s.size(); i++){
					if (cur.s.get(i).compareTo(s)>0){
						cur.s.add(i, s);
						cur.val.add(i, 1);
						if (cur.max_value<cur.val.get(i)){
							cur.max_s = s;
							cur.max_value = cur.val.get(i);
						}
						don++;
						break;
					}
				}
				if (don==0){
					cur.s.add(s);
					cur.val.add(1);
					if (cur.max_value<1){
						cur.max_s = s;
						cur.max_value = 1;
					}
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
			resolve(cur, par);
		}
	}

	public void insert(String s){
		// TO be completed by students
		ins(s, root, null);
	}

	public void incr(String s, TreeNode cur){
		if (cur != null){
			int don = 0;
			for (int i=0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s);
				if (comp>0){
					incr(s, cur.children.get(i));
					don++;
					break;
				} else if (comp==0){
					int valo = cur.val.get(i);
					cur.val.set(i, valo+1);
					don++;
					break;
				}
			}
			if (don == 0){
				if (cur.children.size()==0){
					// System.out.println("eror");
				} else {
					incr(s, cur.children.get(cur.children.size()-1));
				}
			}
			upd(cur);
		}
	}

	public int increment(String s){
		// TO be completed by students
		// System.out.println("incr "+s);
		incr(s, root);
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
					return cur.val.get(i);
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

	public void decr(String s, TreeNode cur){
		if (cur != null){
			int don = 0;
			for (int i=0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s);
				if (comp>0){
					decr(s, cur.children.get(i));
					don++;
					break;
				} else if (comp==0){
					int valo = cur.val.get(i);
					cur.val.set(i, valo-1);
					don++;
					break;
				}
			}
			if (don == 0){
				if (cur.children.size()==0){
					// System.out.println("eror");
				} else {
					decr(s, cur.children.get(cur.children.size()-1));
				}
			}
			upd(cur);
		}
	}

	public int decrement(String s){
		// TO be completed by students
		// System.out.println("dec "+s);
		decr(s, root);
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
					return jk;
				}
			}
			if (don==0){
				if (cur.children.size()==0){
					// System.out.println(" eroor");
					// assert false:"Decrement called on non-existent String";
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

	public void buildTree(String fileName){
		// TO be completed by students
		try {
			File file = new File(fileName);
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()){
				String jk = sc.nextLine();
				String cur = "";
				for (int i=0; i<jk.length(); i++){
					if (jk.charAt(i)==' '){
						if (cur.length()>0){
							if (search(cur)==false){
								insert(cur);
							} else {
								increment(cur);
							}
						}
						cur = "";
					} else {
						cur+=jk.charAt(i);
					}
				}
				if (cur.length()>0){
					if (search(cur)==false){
						insert(cur);
					} else {
						increment(cur);
					}
				}
			}
		} catch (FileNotFoundException e){
			// System.out.println(e);
		}
	}

	public int freqlt(String s1, TreeNode cur){
		int freq = 0;
		if (cur==null){
			return 0;
		} else {
			int don = 0;
			for (int i=0; i<cur.s.size(); i++){
				int comp = cur.s.get(i).compareTo(s1);
				if (comp>0){
					freq+=freqlt(s1, cur.children.get(i));
					don++;
					break;
				} else if (comp==0){
					if (cur.children.size()!=0){
						freq-=cur.children.get(i).count;
						don++;
						break;
					} else {
						don++;
						break;
					}
				} else if (comp<0){
					freq-=cur.children.get(i).count;
					freq-=cur.val.get(i);
				}
			}
			if (don==0){
				freq+=freqlt(s1, cur.children.get(cur.children.size()-1));
			}
		}
		return freq;
	}

	public int cumulativeFreq(String s1, String s2){
		// TO be completed by students
		// System.out.println(s1+" "+s2+"  count");
		// preorder();
		int cur = root.count+freqlt(s1, root);
		int cur2 = root.count+freqlt(s2, root);
		// System.out.println(cur+" "+cur2+" "+getVal(s1));
		return cur-cur2+getVal(s1);
	}

	public String lsmx(String s, int co, int hi, String s1){
		if (co<hi){
			return s1;
		} else if (co==hi){
			int comp = s.compareTo(s1);
			if (s.compareTo("")==0 || comp>0){
				return s1;
			}
			return s;
		}
		return s;
	}

	public int max(int a1, int a2){
		return (a1>a2?a1:a2);
	}

	public String freqmx(String s1, String s2){
		int co = -1;
		String s = "";
		TreeNode rt = root;
		int fo = 0, eq1=0, eq2=0, ind = 0;
		while (rt!=null && fo==0){
			int don = 0;
			for (int i = 0; i<rt.s.size(); i++){
				int comp1 = rt.s.get(i).compareTo(s1);
				int comp2 = rt.s.get(i).compareTo(s2);
				// System.out.println(comp1+" "+comp2+" "+rt.s.get(i));
				if (comp1>0 && comp2>0){
					rt = rt.children.get(i);
					don++;
					break;
				} else if (comp1>=0 && comp2<=0){
					don++;
					fo++;
					if (comp1==0){
						eq1++;
					}
					if (comp2==0){
						eq2++;
					}
					ind = i;
					break;
				} else {
					continue;
				}
			}
			if (don==0){
				// System.out.println(rt.s.get(0)+" okl "+don+"  "+fo);
				rt = rt.children.get(rt.children.size()-1);
			}
		}
		// System.out.println(eq1+" "+eq2);
		if (eq1==1 && eq2==1){
			return rt.s.get(ind);
		} else if (eq1==1){
			TreeNode cur = rt.children.get(ind);
			s = lsmx(s, co, rt.val.get(ind), rt.s.get(ind));
			co = max(co, rt.val.get(ind));
			fo=0;
			while (fo==0 && cur!=null){
				int don = 0;
				for (int i=0; i<cur.s.size(); i++){
					int comp = cur.s.get(i).compareTo(s2);
					if (comp>0){
						cur = cur.children.get(i);
						don++;
						break;
					} else if (comp==0){
						fo++;
						don++;
						if (co<cur.val.get(i)){
							co = cur.val.get(i);
							s = cur.s.get(i);
						} else if (co==cur.val.get(i)){
							if (s.compareTo("")==0 || s.compareTo(cur.s.get(i))>0){
								s = cur.s.get(i);
							}
						}
						if (cur.children.size()!=0){
							TreeNode cir = cur.children.get(i);
							if (cir.max_value>co){
								co = cir.max_value;
								s = cir.max_s;
							} else if (cir.max_value==co){
								if (s.compareTo("")==0 || s.compareTo(cir.max_s)>0){
									s = cir.max_s;
								}
							}
						}
					} else {
						s = lsmx(s, co, cur.val.get(i), cur.s.get(i));
						co = max(co, cur.val.get(i));
						if (cur.children.size()!=0){
							TreeNode hi = cur.children.get(i);
							if (co<hi.max_value){
								co = hi.max_value;
								s = hi.max_s;
							} else if (co==hi.max_value){
								if (s.compareTo("")==0 || s.compareTo(hi.max_s)>0){
									s = hi.max_s;
								}
							}
						}
					}
				}
				if (don==0){
					s = lsmx(s, co, cur.val.get(cur.val.size()-1), cur.s.get(cur.s.size()-1));
					co = max(co, cur.val.get(cur.val.size()-1));
					cur = cur.children.get((cur.children.size()-1));
				}
			}
		} else if (eq2 == 1){
			TreeNode cur = rt.children.get(ind);
			fo = 0;
			s = lsmx(s, co, rt.val.get(ind), rt.s.get(ind));
			co = max(co, rt.val.get(ind));
			while (fo==0 && cur!=null){
				int don = 0;
				for (int i=cur.s.size()-1; i>=0; i--){
					int comp = cur.s.get(i).compareTo(s1);
					// System.out.println(cur.s.get(i)+" ok "+s1);
					if (comp<0){
						cur = cur.children.get(i+1);
						don++;
						break;
					} else if (comp==0){
						fo++;
						don++;
						if (co<cur.val.get(i)){
							co = cur.val.get(i);
							s = cur.s.get(i);
						} else if (co==cur.val.get(i)){
							if (s.compareTo("")==0 || s.compareTo(cur.s.get(i))>0){
								s = cur.s.get(i);
							}
						}
						if (cur.children.size()!=0){
							TreeNode cir = cur.children.get(i+1);
							if (cir.max_value>co){
								co = cir.max_value;
								s = cir.max_s;
							} else if (cir.max_value==co){
								if (s.compareTo("")==0 || s.compareTo(cir.max_s)>0){
									s = cir.max_s;
								}
							}
						}
						break;
					} else {
						s = lsmx(s, co, cur.val.get(i), cur.s.get(i));
						co = max(co, cur.val.get(i));
						if (cur.children.size()!=0){
							TreeNode hi = cur.children.get(i);
							if (co<hi.max_value){
								co = hi.max_value;
								s = hi.max_s;
							} else if (co==hi.max_value){
								if (s.compareTo("")==0 || s.compareTo(hi.max_s)>0){
									s = hi.max_s;
								}
							}
						}
					}
				}
				if (don==0){
					s = lsmx(s, co, cur.val.get(0), cur.s.get(0));
					co = max(co, cur.val.get(0));
					cur = cur.children.get(0);
				}
			}
		} else {
			TreeNode cur = rt;
			fo=0;
			while (fo==0 && cur!=null){
				int don = 0;
				for (int i=0; i<cur.s.size(); i++){
					int comp = cur.s.get(i).compareTo(s2);
					if (comp>0){
						cur = cur.children.get(i);
						don++;
						break;
					} else if (comp==0){
						fo++;
						don++;
						if (co<cur.val.get(i)){
							co = cur.val.get(i);
							s = cur.s.get(i);
						} else if (co==cur.val.get(i)){
							if (s.compareTo("")==0 || s.compareTo(cur.s.get(i))>0){
								s = cur.s.get(i);
							}
						}
						if (cur.children.size()!=0){
							TreeNode cir = cur.children.get(i+1);
							if (cir.max_value>co){
								co = cir.max_value;
								s = cir.max_s;
							} else if (cir.max_value==co){
								if (s.compareTo("")==0 || s.compareTo(cir.max_s)>0){
									s = cir.max_s;
								}
							}
						}
					} else {
						s = lsmx(s, co, cur.val.get(i), cur.s.get(i));
						co = max(co, cur.val.get(i));
						if (cur.children.size()!=0){
							TreeNode hi = cur.children.get(i);
							if (co<hi.max_value){
								co = hi.max_value;
								s = hi.max_s;
							} else if (co==hi.max_value){
								if (s.compareTo("")==0 || s.compareTo(hi.max_s)>0){
									s = hi.max_s;
								}
							}
						}
					}
				}
				if (don==0){
					s = lsmx(s, co, cur.val.get(cur.val.size()-1), cur.s.get(cur.s.size()-1));
					co = max(co, cur.val.get(cur.val.size()-1));
					cur = cur.children.get((cur.children.size()-1));
				}
			}
			cur = rt;
			fo = 0;
			while (fo==0 && cur!=null){
				int don = 0;
				for (int i=cur.s.size()-1; i>=0; i--){
					int comp = cur.s.get(i).compareTo(s1);
					if (comp<0){
						cur = cur.children.get(i+1);
						don++;
						break;
					} else if (comp==0){
						fo++;
						don++;
						if (co<cur.val.get(i)){
							co = cur.val.get(i);
							s = cur.s.get(i);
						} else if (co==cur.val.get(i)){
							if (s.compareTo("")==0 || s.compareTo(cur.s.get(i))>0){
								s = cur.s.get(i);
							}
						}
						if (cur.children.size()!=0){
							TreeNode cir = cur.children.get(i);
							if (cir.max_value>co){
								co = cir.max_value;
								s = cir.max_s;
							} else if (cir.max_value==co){
								if (s.compareTo("")==0 || s.compareTo(cir.max_s)>0){
									s = cir.max_s;
								}
							}
						}
						break;
					} else {
						s = lsmx(s, co, cur.val.get(i), cur.s.get(i));
						co = max(co, cur.val.get(i));
						if (cur.children.size()!=0){
							TreeNode hi = cur.children.get(i);
							if (co<hi.max_value){
								co = hi.max_value;
								s = hi.max_s;
							} else if (co==hi.max_value){
								if (s.compareTo("")==0 || s.compareTo(hi.max_s)>0){
									s = hi.max_s;
								}
							}
						}
					}
				}
				if (don==0){
					s = lsmx(s, co, cur.val.get(0), cur.s.get(0));
					co = max(co, cur.val.get(0));
					cur = cur.children.get(0);
				}
			}
		}
		return s;
	}

	public String maxFreq(String s1, String s2){
		// TO be completed by students
		// System.out.println("  maaxferq  "+ " pl "+s1+" "+s2);
		return freqmx(s1, s2);
	}

}

