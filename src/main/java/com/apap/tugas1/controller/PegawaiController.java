package com.apap.tugas1.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.apap.tugas1.model.InstansiModel;
import com.apap.tugas1.model.JabatanModel;
import com.apap.tugas1.model.PegawaiModel;
import com.apap.tugas1.model.ProvinsiModel;
import com.apap.tugas1.service.InstansiService;
import com.apap.tugas1.service.JabatanService;
import com.apap.tugas1.service.PegawaiService;
import com.apap.tugas1.service.ProvinsiService;

@Controller
public class PegawaiController {
	@Autowired
	private PegawaiService pegawaiService;
	
	@Autowired
	private ProvinsiService provinsiService;
	
	@Autowired
	private JabatanService jabatanService;
	
	@Autowired
	private InstansiService instansiService;
	
	@RequestMapping("/")
	private String home(Model model) {
		model.addAttribute("listJabatan", jabatanService.getAllJabatan());
		model.addAttribute("listInstansi", instansiService.getAllInstansi());
		model.addAttribute("title", "Home Page");
		return "home";
	}

	@RequestMapping("pegawai")
	private String viewPegawaiByNip(@RequestParam(value = "nip") String nip, Model model) {
		PegawaiModel pegawai = pegawaiService.getPegawaiDetailByNip(nip);
		
		// hitung gaji
		double gajiPokok = 0;
		for(JabatanModel jabatan: pegawai.getListJabatan()) {
			if(jabatan.getGajiPokok() > gajiPokok) {
				gajiPokok = jabatan.getGajiPokok();
			}
		}
		double presentaseTunjangan = pegawai.getInstansi().getProvinsi().getPresentaseTunjangan();
		double gaji = gajiPokok + (presentaseTunjangan / 100 * gajiPokok);
		// END hitung gaji
		
		model.addAttribute("pegawai", pegawai);
		model.addAttribute("gaji", (int)gaji);
		model.addAttribute("title", "Detail Pegawai");
		return "detail-pegawai";
	}
	
	@RequestMapping("/pegawai/tambah")
	private String addPegawai(Model model) {
		
		PegawaiModel pegawai = new PegawaiModel();
		List<JabatanModel> jabatanPegawai = new ArrayList<>();
		pegawai.setListJabatan(jabatanPegawai);
		model.addAttribute("pegawai", pegawai);
		
		List<ProvinsiModel> listProvinsi = provinsiService.getAllProvinsi();
		model.addAttribute("listProvinsi", listProvinsi);
		
		List<JabatanModel> listJabatan = jabatanService.getAllJabatan();
		model.addAttribute("listJabatan", listJabatan);
		
		model.addAttribute("title", "Tambah Pegawai");
		return "add-pegawai";
	}
	
	@RequestMapping(value = "/pegawai/tambah", method = RequestMethod.POST, params = {"save"})
	private String addPegawai(@ModelAttribute PegawaiModel pegawai, @RequestParam(value = "idProvinsi") long idProvinsi, Model model) {
		System.out.println(pegawai.getInstansi());
		System.out.println(pegawai.getInstansi().getId());
		// Set instansi from instansi Id
		InstansiModel instansiTemp = instansiService.getInstansiDetailById(pegawai.getInstansi().getId());
		pegawai.setInstansi(instansiTemp);
		
		// Set NIP by generating
		String nipTemp = pegawaiService.generateNip(pegawai);
		pegawai.setNip(nipTemp);
		
		List<JabatanModel> listJabatanTemp = pegawai.getListJabatan();
		JabatanModel jabatanTemp;
		long idTemp;
		for(int i = 0; i < listJabatanTemp.size(); i++) {
			idTemp = listJabatanTemp.get(i).getId();
			jabatanTemp = jabatanService.getJabatanDetailById(idTemp);
			listJabatanTemp.set(i, jabatanTemp);
		}
		pegawai.setListJabatan(listJabatanTemp);
		
		pegawaiService.add(pegawai);
		model.addAttribute("msg", "Pegawai dengan nama " + pegawai.getNama() +
				" berhasil ditambah dengan NIP " + pegawai.getNip());
		return "message";
	}
	
	@RequestMapping(value = "/getInstansi", method = RequestMethod.GET)
	public @ResponseBody
	List<InstansiModel> findInstansi(@RequestParam(value = "idProvinsi", required = true) long idProvinsi) {
		ProvinsiModel provinsi = provinsiService.getProvinsiDetailById(idProvinsi);
		List<InstansiModel> listInstansi = provinsi.getListInstansi();
		
		// Change listPegawai and provinsi to null to prevent error while converting to JSON
		for(int i = 0; i < listInstansi.size(); i++) {
			listInstansi.get(i).setListPegawai(null);
			listInstansi.get(i).setProvinsi(null);
		}
		return listInstansi;
	}
	
	@RequestMapping(value="/pegawai/termuda-tertua", method=RequestMethod.GET)
	private String viewYoungestAndOldestPegawai(@RequestParam ("idInstansi") long idInstansi, Model model) {
		InstansiModel instansi = instansiService.getInstansiDetailById(idInstansi);
		
		PegawaiModel youngest = pegawaiService.getYoungestPegawai(instansi.getListPegawai());
		PegawaiModel oldest = pegawaiService.getOldestPegawai(instansi.getListPegawai());
		
		model.addAttribute("youngest", youngest);
		model.addAttribute("oldest", oldest);
		model.addAttribute("title", "Detail Pegawai");
		return "view-youngest-oldest-pegawai";
	}
}
