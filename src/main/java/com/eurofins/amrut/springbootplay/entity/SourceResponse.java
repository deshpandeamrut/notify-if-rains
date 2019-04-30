package com.eurofins.amrut.springbootplay.entity;

import java.util.List;

public class SourceResponse {
	private List<Source> sources;

	public List<Source> getSources() {
		return sources;
	}

	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

	@Override
	public String toString() {
		return "Response [sources=" + sources + "]";
	}
}
