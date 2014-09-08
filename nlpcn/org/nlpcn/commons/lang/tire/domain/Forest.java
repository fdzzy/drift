package org.nlpcn.commons.lang.tire.domain;


import org.nlpcn.commons.lang.tire.GetWord;

public class Forest implements WoodInterface {
	private WoodInterface[] chars = new WoodInterface[65536];


	@Override
	public WoodInterface add(WoodInterface branch) {
		WoodInterface temp = this.chars[branch.getC()];
		if (temp == null)
			this.chars[branch.getC()] = branch;
		else {
			switch (branch.getStatus()) {
				case 1:
					if (temp.getStatus() == 3) {
						temp.setStatus(2);
					}
					break;
				case 3:
					if (temp.getStatus() == 1) {
						temp.setStatus(2);
					}
					temp.setParam(branch.getParams());
			}
		}

		return this.chars[branch.getC()];
	}

	@Override
	public boolean contains(char c) {
		return this.chars[c] != null;
	}

	@Override
	public WoodInterface get(char c) {
		if (c > 66535) {
			System.out.println(c);
			return null;
		}
		return this.chars[c];
	}


	@Override
	public int compareTo(char c) {
		return 0;
	}

	@Override
	public boolean equals(char c) {
		return false;
	}

	@Override
	public char getC() {
		return '\000';
	}

	public int getNature() {
		return 0;
	}

	public void setNature(int nature) {
	}

	@Override
	public byte getStatus() {
		return 0;
	}

	@Override
	public void setStatus(int status) {
	}

	public int getSize() {
		return this.chars.length;
	}

	@Override
	public String[] getParams() {
		return null;
	}

	@Override
	public void setParam(String[] param) {
	}

	/**
	 * 得到一个分词对象
	 *
	 * @param content
	 * @return
	 */
	public GetWord getWord(String content) {
		return new GetWord(this, content);
	}

	/**
	 * 得到一个分词对象
	 *
	 * @param content
	 * @return
	 */
	public GetWord getWord(char[] chars) {
		return new GetWord(this, chars);
	}

	/**
	 * 清空树释放内存
	 */
	public void clear() {
		chars = new WoodInterface[65535];
	}
}