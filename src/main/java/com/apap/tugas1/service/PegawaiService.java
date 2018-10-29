package com.apap.tugas1.service;

import java.util.List;

import com.apap.tugas1.model.PegawaiModel;

public interface PegawaiService {
	PegawaiModel getPegawaiDetailByNip(String nip);
	PegawaiModel getOldestPegawai(List<PegawaiModel> listPegawai);
	PegawaiModel getYoungestPegawai(List<PegawaiModel> listPegawai);
	void add(PegawaiModel pegawai);
	String generateNip(PegawaiModel pegawai);
}
