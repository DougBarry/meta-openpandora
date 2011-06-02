DESCRIPTION = "Helper uboot boot scripts for the OpenPandora"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

DEPENDS = "u-boot-mkimage-native"

SRC_URI = " \
	file://boot* \
	file://LICENSE \
"

PR = "r4"

do_configure() {
	cp ${WORKDIR}/boot* ${S}
}

do_compile() {
	for i in boot* ; do
		uboot-mkimage -A ${TARGET_ARCH} -O linux -T script -C none -a 0 -e 0 -n "OpenPandora $i" -d $i $i.scr
	done
}

do_install() {
	install -d ${D}${datadir}/u-boot-scripts
	for i in *.scr ; do
		install -m 0644 $i ${D}${datadir}/u-boot-scripts
	done
}

FILES_${PN} += "${datadir}"

addtask deploy before do_package after do_install

do_deploy() {
	install -d ${DEPLOY_DIR_IMAGE}
	for i in *.scr ; do
		cp $i ${DEPLOY_DIR_IMAGE}
		package_stagefile_shell ${DEPLOY_DIR_IMAGE}/$i
	done
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
