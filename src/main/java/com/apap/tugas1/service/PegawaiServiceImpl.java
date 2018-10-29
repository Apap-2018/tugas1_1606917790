package com.apap.tugas1.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.repository.PegawaiDb;

@Service
@Transactional
public class PegawaiServiceImpl implements PegawaiService {
	@Autowired
	private PegawaiDb pegawaiDb;
	
	@Override
	public PegawaiModel getPegawaiDetailByNip(String nip) {
		return pegawaiDb.findByNip(nip);
	}
	
	@Override
	public PegawaiModel getOldestPegawai(List<PegawaiModel> listPegawai) {
		PegawaiModel oldest = null;
		
		for(PegawaiModel pegawai: listPegawai) {
			if(oldest == null) {
				oldest = pegawai;
			}
			else {
				if(pegawai.getTanggalLahir().compareTo(oldest.getTanggalLahir()) < 0)
					oldest = pegawai;
			}
		}

		return oldest;
	}
	
	@Override
	public PegawaiModel getYoungestPegawai(List<PegawaiModel> listPegawai) {
		PegawaiModel youngest = null;
		
		for(PegawaiModel pegawai: listPegawai) {
			if(youngest == null) {
				youngest = pegawai;
			}
			else {
				if(pegawai.getTanggalLahir().compareTo(youngest.getTanggalLahir()) > 0)
					youngest = pegawai;
			}
		}

		return youngest;
	}
	
	@Override
	public void add(PegawaiModel pegawai) {
		pegawaiDb.save(pegawai);
	}
	
	@Override
	 public String generateNip(PegawaiModel pegawai) {
	  String kodeInstansi = String.valueOf(pegawai.getInstansi().getId());
	  String[] dateSplit = String.valueOf(pegawai.getTanggalLahir().toString()).split("-");
	  String tahun = dateSplit[0];
	  String bulan = dateSplit[1];
	  String tanggal = dateSplit[2];
	  tahun = tahun.substring(2, 4);
	  String tahunMasuk = pegawai.getTahunMasuk();
	  
	  // generate nomor urut
	  int noUrut;
	  List<PegawaiModel> listPegawai = pegawaiDb.findByInstansiAndTanggalLahirAndTahunMasukOrderByNipAsc(pegawai.getInstansi(), pegawai.getTanggalLahir(), pegawai.getTahunMasuk());
	  if(listPegawai.size() == 0) {
		  noUrut = 1;
	  }
	  else {
		  PegawaiModel lastPegawai = listPegawai.get(listPegawai.size() - 1);
		  noUrut =Integer.parseInt(lastPegawai.getNip().substring(14, 16)) + 1;
	  }
	  String noUrutStr;
	  if(noUrut == 0) {
	   noUrutStr = "01";
	  }
	  else if (noUrut < 10) {
	   noUrutStr = "0";
	   noUrutStr += String.valueOf(noUrut);
	  }
	  else {
	   noUrutStr = String.valueOf(noUrut);
	  }
	  // END generate nomor urut
	  String nip = kodeInstansi + tanggal + bulan + tahun + tahunMasuk + noUrutStr;
	  return nip;
	 }
}